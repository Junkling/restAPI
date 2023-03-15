package hello.restAPI.web.controller;

import hello.restAPI.domain.user.User;
import hello.restAPI.web.dto.LoginDto;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    @PostMapping("/join")
    public ResponseEntity<String> add(@RequestBody UserCreateDto dto) {
        System.out.println("아이디 패스워드" + dto.getAccountId() + "," + dto.getPassword());

        userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("가입 완료되었습니다.");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        System.out.println("아이디 패스워드" + loginDto.getAccountId() + "," + loginDto.getPassword());
        User loginUser = userService.login(loginDto.getAccountId(), loginDto.getPassword());
        String jwt = userService.authenticate(loginDto.getAccountId(), loginDto.getPassword());
        return ResponseEntity.ok().body(jwt);
    }

    @GetMapping("/delete")
    public String delete(@PathVariable Long id) {

        try {
            userService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            return "삭제 실패하였습니다(해당 id는 존재하지 않습니다.)";
        }
        return "users/delete";

    }


}
