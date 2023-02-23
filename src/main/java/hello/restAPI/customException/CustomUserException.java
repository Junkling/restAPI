package hello.restAPI.customException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomUserException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;
}
