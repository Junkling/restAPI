package hello.restAPI.web.service.user;

import hello.restAPI.domain.user.User;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User save(UserCreateDto user) {
        User user1 = new User();
        user1.setAccountId(user.getAccountId());
        user1.setPassword(user.getPassword());
        return userRepository.save(user1);
    }

    public void delete(Long userId) throws EntityNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        user.setQuite(false);
    }

    public Optional<User> findById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user;
    }


}
