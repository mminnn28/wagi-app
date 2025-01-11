package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.UserRepository;
import wagi_app.wagi.service.AttendanceService;
import wagi_app.wagi.service.UserService;

import java.time.LocalDate;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    //출석 시작 화면
    @GetMapping("/attendance")
    public String requestAttendance() {
        return "attendance1";
    }

    //출석 요청 화면
    @GetMapping("/attendance/request")
    public String requestAttendanceCode(Model model) {
        model.addAttribute("attendanceCreateDTO", new AttendanceCreateDTO());
        return "attendance2";
    }

    //출석 인증 화면
    @PostMapping("/attendance/request")
    public String submitAttendanceCode(@ModelAttribute AttendanceCreateDTO attendanceCreateDTO, Model model) {

        if (attendanceCreateDTO.getAttendance() == 1) {
            return "redirect:/attendance/request/success";
        } else {
            return "redirect:/attendance/request/fail";
        }
    }

    //출석 성공 화면
    @GetMapping("/attendance/request/success")
    public String attendanceSuccess() {
        return "attendance4";
    }

    //출석 결과 화면
    @GetMapping("attendance/result")
    public String attendanceResult(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("M/d"));
        model.addAttribute("todayDate", formattedDate);

        return "attendance3";
    }
    
    //출석 인증번호 생성 화면
    @GetMapping("/admin/attendance")
    public String showAttendanceCode(User user, Model model) {
        int attendanceCode = attendanceService.createAttendanceCode(); // 인증번호 생성
        model.addAttribute("attendanceCode", attendanceCode); // 모델에 데이터 추가

        if (user.getRole() == "ADMIN") {
            return "attendanceCode";
        }
        else {
            return "accessDenied";
        }
    }

}
