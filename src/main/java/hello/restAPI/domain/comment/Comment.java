package hello.restAPI.domain.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hello.restAPI.domain.post.Post;
import hello.restAPI.domain.user.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "댓글 내용을 입력해주세요")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"post"})
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp created;

    @UpdateTimestamp
    private Timestamp updated;

}
