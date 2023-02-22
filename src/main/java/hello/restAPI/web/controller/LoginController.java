package hello.restAPI.web.controller;

import hello.restAPI.web.dto.LoginDto;
import hello.restAPI.web.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    //    @GetMapping("/login")
//    public String loginForm(HttpServletRequest request) {
//        String referrer = request.getHeader("Referer");
//        request.getSession().setAttribute("prevPage", referrer);
//        return "/login/SecurityLoginForm";
//    }
//
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        System.out.println("아이디 패스워드" + loginDto.getAccountId() + "," + loginDto.getPassword());

        String authId = loginService.authId(loginDto.getAccountId(), loginDto.getPassword());
        if (authId != null) {
            return ResponseEntity.ok().body(authId);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디, 패스워드가 틀렸습니다.");
    }
}
