package wagi_app.wagi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String signup(@ModelAttribute @Valid UserCreateDto userCreateDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "모든 필드를 올바르게 입력해주세요.");
            return "user/join";
        }

        if (!userCreateDto.getPassword().equals(userCreateDto.getPassword2())) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "user/join"; // 비밀번호 불일치 시
        }

        try {
            userService.saveUser(userCreateDto);
            return "redirect:/login"; // 성공하면 로그인 페이지로 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다.<br>" + e.getMessage());
            return "user/join"; // 에러 메시지 띄우기
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다.<br>" + e.getMessage());
            return "user/join";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입에 실패했습니다.");
            return "user/join";
        }
    }

}
