package hello.restAPI.web.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String accountId;

    private String password;
}
