package hello.restAPI.customException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ACCOUNT_ID_DUPLICATED(HttpStatus.CONFLICT,"중복");

    private HttpStatus httpStatus;
    private String message;
    }
