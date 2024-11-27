package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.UserCreateDto;
import wagi_app.wagi.service.UserService;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UserCreateDto userCreateDto, Model model) {
        try {
            System.out.println("Signup method reached");
            userService.saveUser(userCreateDto);
            return "redirect:/login"; // 성공하면 로그인 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다. " + e.getMessage());
            return "signup"; // 실패하면 다시 회원가입 페이지로
        }
    }

}
