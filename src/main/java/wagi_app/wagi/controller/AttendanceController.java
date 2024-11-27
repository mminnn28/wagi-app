package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.service.AttendanceService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    //출석 요청 화면
    @GetMapping("/attendance")
    public String requestAttendance() {
        return "attendance";
    }

    //출석 요청 화면
    @GetMapping("/attendance/request")
    public String requestAttendanceCode() {
        return "attendance2";
    }

    //출석 인증번호 입력 화면
    @PostMapping("/attendance/request")
    public String submitAttendanceCode(@RequestBody AttendanceCreateDTO attendanceCreateDTO, Model model) {

        if (attendanceCreateDTO.getAttendance() == 1) {
            return "redirect:/attendance/request/success";
        } else {
            return "redirect:/attendance/request/fail";
        }
    }

    //출석 실패 화면
    @GetMapping("/attendance/request/fail")
    public String attendanceFail() {
        return "attendance3";
    }

    //출석 완료 화면
    @GetMapping("/attendance/request/success")
    public String attendanceSuccess() {
        return "attendance4";
    }


//    //출석 결과 화면
//    @GetMapping("attendance/result")
//    public String attendanceResult(@RequestParam String studentId, Model model) {
//        Optional<Attendance> attendanceOptional = attendanceService.getAttendanceStatus(studentId);
//
//        if (attendanceOptional.isPresent()) {
//            Attendance attendance = attendanceOptional.get();
//            if (attendance.getAttendanceStatus() == null) {
//                model.addAttribute("status", "결석입니다.");
//            } else {
//                model.addAttribute("status", "출석입니다."); //추후 출석 스탬프로 바꿀 예정
//            }
//        } else {
//            model.addAttribute("status", "출석 정보가 없습니다.");
//        }
//
//        return "attendanceResult";
//    }
}
