package hello.restAPI.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserCreateDto {

    @NotEmpty(message = "아이디를 입력하세요")
    private String accountId;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

}
