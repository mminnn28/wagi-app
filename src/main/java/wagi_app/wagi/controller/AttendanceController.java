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
import wagi_app.wagi.DTO.UserDto;
import wagi_app.wagi.Manager.AttendanceCodeManager;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.UserRepository;
import wagi_app.wagi.service.AttendanceService;
import wagi_app.wagi.service.UserService;

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
    private final UserService userService;
    private final AttendanceCodeManager attendanceCodeManager;

    //출석 시작 화면
    @GetMapping("/attendance")
    public String requestAttendance(Model model) {
        String correctCode = attendanceCodeManager.getAttendanceCode();
        if (correctCode == null) {
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

            return "attendance/attendance3";// 관리자가 생성한 인증코드 만료 시 출석 현황? 확인하는 화면으로 이동
        }
        // 관리자가 생성한 출석 인증 코드가 유효한 경우 출석 시작 화면으로 이동
        UserDto userDto = userService.getUserInfo();
        model.addAttribute("username", userDto.getUsername());
        model.addAttribute("studentId", userDto.getStudentId());

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

        String correctCode = attendanceCodeManager.getAttendanceCode();

        System.out.println("Input Code: [" + attendanceCreateDTO.getInputCode() + "]");
        System.out.println("Correct Code: [" + correctCode + "]");

        System.out.println("Input Code Length: " + attendanceCreateDTO.getInputCode().length());
        System.out.println("Correct Code Length: " + correctCode.length());


        if (correctCode == null) {
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

            return "attendance/attendance3";// 관리자가 생성한 인증코드 만료 시 출석 현황? 확인하는 화면으로 이동
        }
        if (correctCode.equals(attendanceCreateDTO.getInputCode())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                attendanceCreateDTO.setUserId(user.getUserId());
                attendanceService.createAttendance(attendanceCreateDTO);
                return "attendance/attendance4"; // 출석 성공
            }
        }
        System.out.println("인증 코드가 일치하지 않음");
        return "attendance/attendance2"; // 다시 입력 페이지로 이동

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
        }

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
