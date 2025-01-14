package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.UserDto;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.service.AdminService;
import wagi_app.wagi.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/admin/register")
    public String registerUserPage() {
        return "manager/manager03";
    }

    @PostMapping("/admin/register")
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

    @GetMapping("/admin/auth")
    public String auth(Model model) {
        List<UserDto> members = userService.getUserList();
        model.addAttribute("members", members);
        return "manager/manager04";
    }

    @PostMapping("/admin/updateRole")
    public String updateRole(@RequestBody UserDto userDto, Model model) {
        try {
            userService.updateRole(userDto);
            model.addAttribute("message", "권한이 변경되었습니다.");
            return "manager/manager04";

        } catch (Exception e) {
            model.addAttribute("message", "사용자를 찾을 수 없습니다.");
            return "manager/manager04";
        }


    }
}
