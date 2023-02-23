package hello.restAPI.web.dto;


import javax.validation.constraints.NotEmpty;

public class PostDto {
    @NotEmpty(message = "제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요")
    private String contents;

    public PostDto() {
    }
}
