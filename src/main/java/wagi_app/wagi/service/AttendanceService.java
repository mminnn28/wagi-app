package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.Manager.AttendanceCodeManager;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.repository.AttendanceRepository;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceCodeManager attendanceCodeManager;


    public String createAttendanceCode() {
        SecureRandom secureRandom = new SecureRandom();
        int attendanceCode = 10000 + secureRandom.nextInt(90000); //10000~99999 사이의 숫자 생성
        System.out.println("Generated Attendance Code: " + attendanceCode);  // 로그 출력
        attendanceCodeManager.generateAttendanceCode(String.valueOf(attendanceCode));
        return String.valueOf(attendanceCode);  // int 값을 String으로 변환하여 반환
    }

    //출석 인증번호 확인 및 출석 처리
    public String createAttendance(AttendanceCreateDTO attendanceCreateDTO) {
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceCreateDTO.getUserId());
        attendance.setAttendance("1");
        attendanceRepository.save(attendance);
        return attendanceCreateDTO.getAttendance();
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }
}

