package hello.restAPI.web.controller;

import hello.restAPI.customException.PostUserException;
import hello.restAPI.domain.comment.Comment;
import hello.restAPI.domain.post.Post;
import hello.restAPI.web.dto.PostUpdateDto;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.service.heart.HeartService;
import hello.restAPI.web.service.post.PostService;
import hello.restAPI.web.service.user.UserService;
import hello.restAPI.web.utils.HeaderCheck;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    private final HeartService heartService;
    private final UserService userService;

    private final HeaderCheck headerCheck;

    @GetMapping("/test")
    public ResponseEntity<Object> list(@RequestBody Object post ,@RequestHeader HttpHeaders headers) {

        System.out.println("Post=" + post);
        String s = getAuth(headers);
        System.out.println(s);
        System.out.println("id=" + headerCheck.splitId(s));
        System.out.println("auth="+ headerCheck.checkAuth(s));
        return ResponseEntity.ok(post);
    }

    @GetMapping("/list")
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

//    @GetMapping("/add")
//    public String addForm(@ModelAttribute Post post, @RequestHeader HttpHeaders headers) {
//        if (headerCheck.checkAuth(headers.toString()) == true) {
//            return "post/addForm";
//        }
//        return null;
//    }

    @PostMapping("/add")
    public ResponseEntity<Post> addPost(@Valid @RequestBody Post post, @RequestHeader HttpHeaders headers) {
        String auth = getAuth(headers);
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Long aLong = headerCheck.splitId(auth);
        User user = userService.findById(aLong).orElseThrow();
        Post savedPost = postService.save(post, user);

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
