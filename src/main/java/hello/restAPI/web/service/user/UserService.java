package hello.restAPI.web.service.user;

import hello.restAPI.customException.CustomUserException;
import hello.restAPI.customException.ErrorCode;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.repository.user.UserRepository;
import hello.restAPI.web.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;



    public String save(UserCreateDto user) {
        userRepository.findByAccountId(user.getAccountId()).ifPresent(u -> {
            throw new CustomUserException(ErrorCode.ACCOUNT_ID_DUPLICATED,"\n"+ user.getAccountId()+" 는 이미 사용 중입니다.");
        });

        userRepository.findByNickName(user.getNickName()).ifPresent(u -> {
            throw new RuntimeException("닉네임 중복입니다.");
        });
        if (user.getAccountId() == null || user.getPassword() == null) {
            throw new RuntimeException("아이디 패스워드는 필수값입니다.");
        }
        log.info("PW={}", user.getPassword());
        User user1 = User.builder()
                .accountId(user.getAccountId())
                .password(encoder.encode(user.getPassword()))
                .nickName(user.getNickName())
                .roles(Collections.singletonList(user.getRole())) // 최초 가입시 USER 로 설정
                .build();
        userRepository.save(user1);
        return "가입 완료";
    }


    public User login(String accountId, String password) {
        User user = userRepository.findByAccountId(accountId).orElseThrow(() -> new CustomUserException(ErrorCode.USERNAME_NOT_FOUND, "해당 아이디는 없는 아이디입니다."));

        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomUserException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }
        return user;
    }

    public void delete(Long userId) throws EntityNotFoundException {
        User user = userRepository.findById(userId).orElseThrow();
        user.setQuite(false);
    }

    public Optional<User> findById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user;
    }
    public Optional<User> findByAccountId(String userName) {
        Optional<User> user = userRepository.findByAccountId(userName);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByAccountId(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

    }
}
