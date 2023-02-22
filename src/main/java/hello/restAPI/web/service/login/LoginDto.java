package hello.restAPI.web.service.login;


import lombok.Data;

@Data
public class LoginDto {

    private String accountId;

    private String password;
}
