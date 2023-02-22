package hello.restAPI.web.service.comment;

import hello.restAPI.domain.comment.Comment;
import hello.restAPI.domain.post.Post;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.repository.comment.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public Comment save(Comment comment, Post post, User user) {
        comment.setUser(user);
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public void update(Long commentId, Comment comment, User user) throws IOException {
        if(!isWriter(commentId, user)){
            throw new IOException("작성자가 아닙니다.");
        }

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));
        findComment.setContents(comment.getContents());
    }


    public void delete(Long commentId, User user) throws IOException {
        if(!isWriter(commentId, user)){
            throw new IOException("작성자가 아닙니다.");
        }
        commentRepository.delete(
                commentRepository.findById(commentId).orElseThrow(()-> new EntityNotFoundException("댓글이 존재하지 않습니다."))
        );
    }

    private boolean isWriter(Long commentId, User user) {
        return commentRepository.findById(commentId).get().getUser().equals(user);
    }

}
