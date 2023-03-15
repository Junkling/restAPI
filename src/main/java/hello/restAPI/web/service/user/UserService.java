package hello.restAPI.web.service.user;

import hello.restAPI.customException.CustomUserException;
import hello.restAPI.customException.ErrorCode;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.jwt.AccountAdapter;
import hello.restAPI.web.jwt.JwtUtils;
import hello.restAPI.web.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;


    public String authenticate(String username, String password) {
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 그 객체를 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = jwtUtils.createJwt(authentication);

        return accessToken;
    }

    public String save(UserCreateDto user) {
        userRepository.findByAccountId(user.getAccountId()).ifPresent(u -> {
            throw new CustomUserException(ErrorCode.ACCOUNT_ID_DUPLICATED, "\n" + user.getAccountId() + " 는 이미 사용 중입니다.");
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
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findByAccountId(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));

//        if(user.isActivated()) throw new RuntimeException(user.getUsername() + " -> 활성화되어 있지 않습니다.");
        return new AccountAdapter(user);
    }
}
