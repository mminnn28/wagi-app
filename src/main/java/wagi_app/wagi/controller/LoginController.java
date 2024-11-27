package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import wagi_app.wagi.service.UserService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //임시로 객체 말고 모델에 담았어요 나중에 수정 필요?
    @PostMapping("/login")
    public String login(Model model) {
        model.addAttribute("userId", "");
        model.addAttribute("password", "");
        return "login";
    }
}

