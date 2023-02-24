package hello.restAPI.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.restAPI.customException.CustomUserException;
import hello.restAPI.customException.ErrorCode;
import hello.restAPI.web.dto.LoginDto;
import hello.restAPI.web.dto.UserCreateDto;
import hello.restAPI.web.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @WithMockUser
    void join() throws Exception {
        String accountId = "tester123";
        String password = "123123";
        String nickName = "11111";


        mockMvc.perform(post("/api/users/join").
                        with(csrf()).
                        contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(new UserCreateDto(accountId, password,nickName, null))))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("가입 실패 아이디 중복")
    @WithMockUser
    void joinFail() throws Exception {
        String accountId = "BBBB";
        String password = "123123";
        String nickName = "11111";
        UserCreateDto userCreateDto = new UserCreateDto(accountId, password, nickName, null);
        when(userService.save(any())).thenThrow(new RuntimeException("아이디 중복"));

        mockMvc.perform(post("/api/users/join").
                        with(csrf()).
                        contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userCreateDto)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
//    @Test
//    @DisplayName("로그인 성공")
//    @WithMockUser
//    void loginSuccess() throws Exception {
//        String accountId = "CCCC";
//        String password = "3333";
//
//        when(userService.login(any(), any()))
//                .thenReturn("token");
//
//        mockMvc.perform(post("/api/users/login")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new LoginDto(accountId, password))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("로그인 실패 (아이디 없음)")
    @WithMockUser
    void loginFail1() throws Exception {

        String accountId = "123123123";
        String password = "3333";

        when(userService.login(any(), any()))
                .thenThrow(new CustomUserException(ErrorCode.USERNAME_NOT_FOUND, ""));

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(new LoginDto(accountId, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("로그인 실패 (비밀번호 틀림)")
    @WithMockUser
    void loginFail2() throws Exception {
        String accountId = "CCCC";
        String password = "4444";

        when(userService.login(any(), any()))
                .thenThrow(new CustomUserException(ErrorCode.INVALID_PASSWORD,""));

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(new LoginDto(accountId, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}