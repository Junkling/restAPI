package hello.restAPI.web.service.user;

import hello.restAPI.customException.CustomUserException;
import hello.restAPI.customException.ErrorCode;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;


    public void save(UserCreateDto user) {
        userRepository.findByAccountId(user.getAccountId()).ifPresent(u -> {
            throw new CustomUserException(ErrorCode.ACCOUNT_ID_DUPLICATED,"\n"+ user.getAccountId()+" 는 이미 사용 중입니다.");
        });

        userRepository.findByNickName(user.getNickName()).ifPresent(u -> {
            throw new RuntimeException("닉네임 중복입니다.");
        });

        User user1 = new User();
        user1.setAccountId(user.getAccountId());
        user1.setPassword(encoder.encode(user.getPassword()));
        user1.setNickName(user.getNickName());
        user1.setRole(user.getRole());
        userRepository.save(user1);
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
