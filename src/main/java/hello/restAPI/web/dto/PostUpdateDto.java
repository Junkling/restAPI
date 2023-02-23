package hello.restAPI.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class PostUpdateDto {
    @NotEmpty(message = "수정할 제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "수정할 내용을 입력해주세요")
    private String contents;

}
