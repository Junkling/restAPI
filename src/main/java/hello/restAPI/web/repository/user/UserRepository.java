package hello.restAPI.web.repository.user;

import hello.restAPI.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccountId(String accountId);
    Optional<User> findByNickName(String nickName);

}
