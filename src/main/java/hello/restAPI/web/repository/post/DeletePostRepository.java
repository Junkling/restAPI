package hello.restAPI.web.repository.post;

import hello.restAPI.domain.post.DeletedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletePostRepository extends JpaRepository<DeletedPost, Long> {

}
