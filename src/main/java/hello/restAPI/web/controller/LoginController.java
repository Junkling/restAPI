//package hello.restAPI.web.controller;
//
//import hello.restAPI.web.dto.LoginDto;
//import hello.restAPI.web.service.login.LoginService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
//public class LoginController {
//    private final LoginService loginService;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
//        System.out.println("아이디 패스워드" + loginDto.getAccountId() + "," + loginDto.getPassword());
//
//        String token = loginService.login(loginDto.getAccountId(), loginDto.getPassword());
////        String authId = loginService.authId(loginDto.getAccountId(), loginDto.getPassword());
////        if (authId != null) {
////            return ResponseEntity.ok().body(authId);
////        }
//        return ResponseEntity.ok().body(token);
//    }
//}
