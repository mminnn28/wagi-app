package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wagi_app.wagi.service.AdminService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/register")
    public String registerUserPage() {
        return "manager/manager03";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("studentId") String studentId, Model model) {
        try {
            adminService.approvedUser(studentId);
            model.addAttribute("message", "등록되었습니다.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", "등록에 실패했습니다." + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("message", "등록에 실패했습니다.");
        }

        return "manager/manager03";
    }
}
