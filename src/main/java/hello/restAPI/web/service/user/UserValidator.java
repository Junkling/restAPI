package hello.restAPI.web.service.user;

import hello.restAPI.domain.user.User;
import hello.restAPI.web.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserRepository userRepository;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (user.getAccountId() == null) {
            errors.rejectValue("loginId","idEmpty","ID 입력하세요.");
        }
        if (user.getPassword() == null) {
            errors.rejectValue("password","passwordEmpty","PW 입력하세요.");
        }

        if (userRepository.findByAccountId(user.getAccountId()).orElse(null)!=null) {
            errors.rejectValue("loginId","idUnique","ID 중복입니다.");
        }
        if (userRepository.findByNickName(user.getNickName()).orElse(null)!=null) {
            errors.rejectValue("nickName","nickNameUnique","닉네임 중복입니다.");
        }
    }
}
