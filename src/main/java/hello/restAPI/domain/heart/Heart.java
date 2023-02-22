package hello.restAPI.domain.heart;

import hello.restAPI.domain.post.Post;
import hello.restAPI.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
