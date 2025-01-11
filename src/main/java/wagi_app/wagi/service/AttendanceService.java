package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.AttendanceRepository;
import wagi_app.wagi.repository.UserRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    //출석 인증번호 랜덤 5자리 수 생성
    private int attendanceCode;

    public int createAttendanceCode() {
        SecureRandom secureRandom = new SecureRandom();
        attendanceCode = 10000 + secureRandom.nextInt(90000); //10000~99999 사이의 숫자 생성
        return attendanceCode;
    }

    //출석 인증번호 확인 및 출석 처리
    public int createAttendance(AttendanceCreateDTO attendanceCreateDTO) {

        int inputCode = attendanceCreateDTO.getInputCode();

        Attendance attendance = null;

        attendance.setUserId(attendanceCreateDTO.getUserId());

        if (inputCode == attendanceCode) {
            attendance.setAttendance(1); //출석이면 1, 지각 또는 조퇴이면 0
        } else {
            attendance.setAttendance(null); //결석이면 null
        }

        attendanceRepository.save(attendance);

        return attendance.getAttendance();
    }
}

