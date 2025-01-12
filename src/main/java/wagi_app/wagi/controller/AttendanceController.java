package wagi_app.wagi.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.service.AttendanceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@Controller
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    //출석 시작 화면
    @GetMapping("/attendance")
    public String requestAttendance(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String studentId = user.getStudentId();
            String username = user.getUsername();
            model.addAttribute("studentId", studentId);
            model.addAttribute("username", username);
        };

        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        model.addAttribute("todayDate", formattedDate);
        return "attendance/attendence1";
    }

    //출석 요청 화면
    @GetMapping("/attendance/request")
    public String requestAttendanceCode(Model model) {
        model.addAttribute("attendanceCreateDTO", new AttendanceCreateDTO());
        return "attendance/attendance2";
    }

    //출석 인증 화면
    @PostMapping("/attendance/request")
    public String submitAttendanceCode(@ModelAttribute AttendanceCreateDTO attendanceCreateDTO, Model model) {

        if (attendanceCreateDTO.getAttendance() == "1") {
            return "redirect:/attendance/request/success";
        } else {
            return "attendance/attendance2";
        }
    }

    //출석 성공 화면
    @GetMapping("/attendance/request/success")
    public String attendanceSuccess() {
        return "attendance/attendance4";
    }

    //출석 결과 화면
    @GetMapping("/attendance/result")
    public String attendanceResult(User user, Model model) {
        model.addAttribute("user", user);

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("M/d"));
        model.addAttribute("todayDate", formattedDate);

        return "attendance/attendance3";
    }
    
    //출석 인증번호 생성 화면
    @GetMapping("/admin/attendance")
    public String createAttendanceCode() {
        return "attendance/manager1";
    }

    //출석 인증번호 공개 화면
    @PostMapping("/admin/attendance")
    public String showAttendanceCode(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String role = user.getRole();
            model.addAttribute("role", role);

            if ("ADMIN".equals(role)) {
                String attendanceCode = attendanceService.createAttendanceCode(); // 인증번호 생성
                model.addAttribute("attendanceCode", attendanceCode); // 모델에 데이터 추가
            }
        };
        return "attendance/maneger2";
    }
}
