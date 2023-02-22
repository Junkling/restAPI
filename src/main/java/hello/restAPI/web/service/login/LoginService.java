package hello.restAPI.web.service.login;

import hello.restAPI.domain.user.User;
import hello.restAPI.web.repository.user.UserRepository;
import hello.restAPI.web.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60l;


    public String throwJwt(String accountId, String password) {
        User login = login(accountId, password);
        if (login != null) {
        return JwtUtils.createJwt(login.getAccountId(), secretKey, expiredMs);
        }
        return null;
    }
    public User login(String accountId, String password) {
        User user = userRepository.findByAccountId(accountId).filter(m-> m.getPassword().equals(password)).orElseThrow();

        return user;
    }

    public String authId(String accountId, String password) {
        User user = login(accountId, password);
        if (user != null) {
            return user.getRole()+" "+user.getId();
        }
        return null;
    }

}
