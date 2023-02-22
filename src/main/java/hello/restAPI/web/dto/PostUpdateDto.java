package hello.restAPI.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class PostUpdateDto {
    @NotEmpty(message = "수정할 제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "수정할 내용을 입력해주세요")
    private String contents;

}
