package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import wagi_app.wagi.DTO.UserCreateDto;
import wagi_app.wagi.service.UserService;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm() {
        return "user/join";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UserCreateDto userCreateDto, Model model) {
        try {
            userService.saveUser(userCreateDto);
            return "redirect:/login"; // 성공하면 로그인 페이지로 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다.\n" + e.getMessage());
            return "user/join"; // 에러 메시지 띄우기
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다.\n" + e.getMessage());
            return "user/join";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다.");
            return "user/join";
        }
    }

}
