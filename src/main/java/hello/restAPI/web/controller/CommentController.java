package hello.restAPI.web.controller;

import hello.restAPI.domain.comment.Comment;
import hello.restAPI.domain.post.Post;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.service.comment.CommentService;
import hello.restAPI.web.service.post.PostService;
import hello.restAPI.web.service.user.UserService;
import hello.restAPI.web.utils.HeaderCheck;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    private final HeaderCheck headerCheck;



    @PostMapping("/post/{postId}/comment")
    public String save(Comment comment, @PathVariable Long postId, @RequestHeader HttpHeaders headers) {
        Post post = postService.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시물이 존재하지 않습니다."));
        Long longId = headerCheck.splitId(headers.toString());
        User user = userService.findById(longId).orElseThrow(null);
        commentService.save(comment, post, user);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/comment/update/{commentId}")
    public String update(Comment comment, @PathVariable Long commentId, @RequestHeader HttpHeaders headers) {
        Comment findComment = commentService.findById(commentId).get();
        try {
            Long longId = headerCheck.splitId(headers.toString());
            User user = userService.findById(longId).orElseThrow(null);
            commentService.update(commentId, comment, user);
        } catch (IOException e) {
            return "/comment/updateFail";
        }

        return "post/list";
    }

    @Transactional
    @GetMapping("/comment/delete/{commentId}")
    public String deleteComment(@RequestHeader HttpHeaders headers, @PathVariable Long commentId, RedirectAttributes redirectAttributes) {
        try {
            Long longId = headerCheck.splitId(headers.toString());
            User user = userService.findById(longId).orElseThrow(null);
            commentService.delete(commentId, user);
        } catch (IOException e) {
            return "/comment/deleteFail";
        }
        return "post/list";
    }
}
