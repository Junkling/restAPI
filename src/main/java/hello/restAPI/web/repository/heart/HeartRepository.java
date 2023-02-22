package hello.restAPI.web.repository.heart;

import hello.restAPI.domain.heart.Heart;
import hello.restAPI.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByUserAndPostId(User user, Long postId);

    Optional<Heart> findByUserId(Long userId);

}
