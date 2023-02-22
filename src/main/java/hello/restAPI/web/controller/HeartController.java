package hello.restAPI.web.controller;

import hello.restAPI.customException.HeartException;
import hello.restAPI.domain.heart.Heart;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.service.heart.HeartService;
import hello.restAPI.web.service.user.UserService;
import hello.restAPI.web.utils.HeaderCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor

public class HeartController {
    private final HeartService heartService;
    private final UserService userService;

    private final HeaderCheck headerCheck;

    @Transactional
    @PostMapping("/post/{postId}/heart")
    public String heart(@ModelAttribute Heart heart, @PathVariable Long postId, @RequestHeader HttpHeaders headers, RedirectAttributes redirectAttributes) throws HeartException, IOException {
        try {
            User user = userService.findById(headerCheck.splitId(headers.toString())).orElseThrow(null);
            heartService.save(heart, postId, user);
        } catch (IOException e) {
            redirectAttributes.addAttribute("heartExist", true);
            return "redirect:/post/{postId}";
        }
        return "redirect:/post/{postId}";
    }

    @Transactional
    @DeleteMapping("/post/{postId}/heart")
    public String unHeart(@PathVariable Long postId, @RequestHeader HttpHeaders headers) throws HeartException {
        User user = userService.findById(headerCheck.splitId(headers.toString())).orElseThrow(null);
        heartService.delete(postId, user);
        return "redirect:/post/{postId}";
    }



}
