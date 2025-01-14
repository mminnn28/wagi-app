package wagi_app.wagi.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.UserRepository;
import wagi_app.wagi.service.AttendanceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

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
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        model.addAttribute("todayDate", formattedDate);
        model.addAttribute("todayTime", formattedTime);
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

        String attendance = attendanceService.createAttendance(attendanceCreateDTO);
        model.addAttribute("inputCode", attendanceCreateDTO.getInputCode());


        if ("1".equals(attendance)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                model.addAttribute("studentId", user.getStudentId());
                model.addAttribute("username", user.getUsername());
            }
            return "attendance/attendance4"; // 성공 페이지
        } else {
            return "attendance/attendance2"; // 실패 시 다시 입력 페이지로 이동
        }

    }

    //출석 성공 화면
    @GetMapping("/attendance/request/success")
    public String attendanceSuccess(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String studentId = user.getStudentId();
            String username = user.getUsername();
            model.addAttribute("studentId", studentId);
            model.addAttribute("username", username);
        };

        return "attendance/attendance4";
    }

    //출석 결과 화면
    @GetMapping("/attendance/result")
    public String attendanceResult(Attendance attendance, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String studentId = user.getStudentId();
            String username = user.getUsername();
            model.addAttribute("studentId", studentId);
            model.addAttribute("username", username);
        };

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("M/d"));
        model.addAttribute("todayDate", formattedDate);

        List<Attendance> attendances = attendanceService.findAll();
        model.addAttribute("attendances", attendances);

        return "attendance/attendance3";
    }
    
    //출석 인증번호 생성 화면
    @GetMapping("/admin/attendance")
    public String createAttendanceCode() {
        return "manager/manager1";
    }

    //출석 인증번호 공개 화면
    @PostMapping("/admin/attendance")
    public String showAttendanceCode(Model model) {

        String attendanceCode = attendanceService.createAttendanceCode(); // 인증번호 생성
        model.addAttribute("attendanceCode", attendanceCode); // 모델에 데이터 추가

        return "manager/maneger2";
    }

}
