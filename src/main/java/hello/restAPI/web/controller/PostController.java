package hello.restAPI.web.controller;

import hello.restAPI.customException.PostUserException;
import hello.restAPI.domain.comment.Comment;
import hello.restAPI.domain.post.Post;
import hello.restAPI.web.dto.PostDto;
import hello.restAPI.web.dto.PostUpdateDto;
import hello.restAPI.web.service.heart.HeartService;
import hello.restAPI.web.service.post.PostService;
import hello.restAPI.web.service.user.UserService;
import hello.restAPI.web.utils.HeaderCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    private final HeartService heartService;
    private final UserService userService;


    private final HeaderCheck headerCheck;

//    @GetMapping("/test")
//    public ResponseEntity<Object> list(@RequestBody Object post ,@RequestHeader HttpHeaders headers) {
//
//        System.out.println("Post=" + post);
//        String s = getAuth(headers);
//        System.out.println(s);
//        System.out.println("id=" + headerCheck.splitId(s));
//        System.out.println("auth="+ headerCheck.checkAuth(s));
//        return ResponseEntity.ok(post);
//    }

    @GetMapping()
    public ResponseEntity<List<Post>> list() {
        List<Post> posts = postService.findPost();
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<HashMap<Post,List<Comment>>> post(@PathVariable Long postId) {
        Post post = postService.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다."));
        List<Comment> comments = post.getComments();

        HashMap<Post, List<Comment>> postListHashMap = new HashMap<>();

        postListHashMap.put(post, comments);

        return ResponseEntity.ok().body(postListHashMap);
    }


    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('Realtor','Lessor','Lessee')")
    //ROLE_Realtor 와 같이 앞에 ROLE_가 붙어야 시큐리티에서 권한을 인식함
    public ResponseEntity<Post> writePost(@RequestBody PostDto post, @AuthenticationPrincipal User user) {
        String accountId = user.getUsername();
        System.out.println("name = " + accountId);
        Post savedPost = postService.save(post, userService.findByAccountId(accountId).orElseThrow());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    private static String getAuth(HttpHeaders headers) {
        List<String> authorization = headers.get("Authorization");
        if (authorization != null) {
        String s = authorization.get(0);
            return s;
        }
        return null;
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId, Model model, @RequestHeader HttpHeaders headers) {
        Post post = postService.findById(postId).get();
        if (postService.checkWriter(userService.findById(Long.valueOf(headerCheck.splitId(headers.toString()))).orElseThrow(null), post) == false) {
            return "redirect:/post/{postId}";
        }
        model.addAttribute("post", post);
        return "/post/editForm";
    }

    @Transactional
    @PatchMapping("/{postId}")
        public ResponseEntity<Post> edit(@PathVariable Long postId, @RequestBody PostUpdateDto updateParam, @RequestHeader HttpHeaders headers) {
            String auth = getAuth(headers);
        if (auth==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            postService.update(postId, updateParam, userService.findById(headerCheck.splitId(auth)).orElseThrow());
            return ResponseEntity.ok().body(postService.findById(postId).orElseThrow());
        } catch (PostUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Transactional
    @GetMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId, @RequestHeader HttpHeaders headers) {
        postService.delete(postId, userService.findById(headerCheck.splitId(headers.toString())).orElseThrow(null));
        return "post/list";
    }
}
