package hello.restAPI.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("가입 성공")
    void join() throws Exception {
        String accountId = "AAAA";
        String password = "123123";
        String nickName = "11111";


        mockMvc.perform(post("/api/users/join").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(new UserCreateDto(accountId, password,nickName, null))))
                .andDo(print())
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("가입 실패 아이디 중복")
    void joinFail() throws Exception {
        String accountId = "AAAA";
        String password = "123123";
        String nickName = "11111";

        mockMvc.perform(post("/api/users/join").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(new UserCreateDto(accountId, password, nickName, null))))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}