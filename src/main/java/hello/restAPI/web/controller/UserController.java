package hello.restAPI.web.controller;

import hello.restAPI.domain.user.User;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.service.user.UserService;
import hello.restAPI.web.service.user.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    @GetMapping("/add")
    public String add(User user) {
        return "users/add";
    }

    @PostMapping("/add")
    public ResponseEntity<String>add(@Valid @RequestBody UserCreateDto user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디 비밀번호를 확인하세요");
        }

        userService.save(user);
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
