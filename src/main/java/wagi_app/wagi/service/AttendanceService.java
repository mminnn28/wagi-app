package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.repository.AttendanceRepository;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    //출석 인증번호 랜덤 5자리 수 생성
    private int attendanceCode;
    public String createAttendanceCode() {
        SecureRandom secureRandom = new SecureRandom();
        attendanceCode = 10000 + secureRandom.nextInt(90000); //10000~99999 사이의 숫자 생성
        System.out.println("Generated Attendance Code: " + attendanceCode);  // 로그 출력
        return String.valueOf(attendanceCode);  // int 값을 String으로 변환하여 반환
    }

    //출석 인증번호 확인 및 출석 처리
    public String createAttendance(AttendanceCreateDTO attendanceCreateDTO) {
        String inputCode = attendanceCreateDTO.getInputCode();
        Attendance attendance = new Attendance();
        attendance.setUserId(attendanceCreateDTO.getUserId());
        if (inputCode == String.valueOf(attendanceCode)) {
            attendance.setAttendance("1"); //출석이면 1, 지각 또는 조퇴이면 2
        } else {
            attendance.setAttendance(null); //결석이면 0
        }
        attendanceRepository.save(attendance);
        return attendanceCreateDTO.getAttendance();
    }
}

