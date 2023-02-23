package hello.restAPI.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDto {

    //    @NotEmpty(message = "아이디를 입력하세요")
    private String accountId;

    //    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

    //    @NotEmpty(message = "닉네임을 입력하세요")
    private String nickName;

    private String role;

}
