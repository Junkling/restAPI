package hello.restAPI.web.controller;

import hello.restAPI.domain.user.User;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.exceptionManager.ExceptionManager;
import hello.restAPI.web.service.user.UserService;
import hello.restAPI.web.service.user.UserValidator;
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
    public ResponseEntity<String>add(@RequestBody UserCreateDto dto) {
            userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("가입 완료되었습니다.");
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
