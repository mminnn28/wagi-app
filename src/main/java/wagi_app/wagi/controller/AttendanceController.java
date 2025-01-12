package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.service.AttendanceService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    //출석 시작 화면
    @GetMapping("/attendance")
    public String requestAttendance() {
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

        if (attendanceCreateDTO.getAttendance() == 1) {
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
    public String attendanceResult(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("M/d"));
        model.addAttribute("todayDate", formattedDate);

        return "attendance/attendance3";
    }
    
    //출석 인증번호 생성 화면
    @GetMapping("/admin/attendance")
    public String createAttendanceCode(User user, Model model) {

        if (user != null && "ADMIN".equals(user.getRole())) {
            int attendanceCode = attendanceService.createAttendanceCode(); // 인증번호 생성
            model.addAttribute("attendanceCode", attendanceCode); // 모델에 데이터 추가
        }
        else {
            model.addAttribute("error", "관리자만 인증번호를 생성할 수 있습니다.");
        }

        return "attendance/manager1";
    }

    //출석 인증번호 공개 화면
    @PostMapping("/admin/attendance")
    public String showAttendanceCode(User user, Model model) {
        return "attendance/maneger2";
    }
}
