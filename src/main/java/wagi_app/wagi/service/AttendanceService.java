package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wagi_app.wagi.DTO.AttendanceCreateDTO;
import wagi_app.wagi.Manager.AttendanceCodeManager;
import wagi_app.wagi.entity.Attendance;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.AttendanceRepository;
import wagi_app.wagi.repository.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceCodeManager attendanceCodeManager;
    private final UserRepository userRepository;

    private Timer timer = new Timer();


    public String createAttendanceCode() {
        SecureRandom secureRandom = new SecureRandom();
        int attendanceCode = 10000 + secureRandom.nextInt(90000); //10000~99999 사이의 숫자 생성
        System.out.println("Generated Attendance Code: " + attendanceCode);  // 로그 출력
        attendanceCodeManager.generateAttendanceCode(String.valueOf(attendanceCode));
        // 1분 뒤에 만료 및 결석 처리
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LocalDate today = LocalDate.now();
                attendanceCodeManager.handleExpiredCode(AttendanceService.this);  // 결석 처리 로직이 있는 메서드~!!
            }
        }, 60000);  // 1분 뒤에 실행

        return String.valueOf(attendanceCode);

    }

    //출석 인증번호 확인 및 출석 처리
    public void createAttendance(AttendanceCreateDTO attendanceCreateDTO) {
        Attendance attendance = new Attendance();
        Optional<User>optionalUser = userRepository.findByUserId(attendanceCreateDTO.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            attendance.setUser(user);
            attendance.setAttendance("1");
            attendanceRepository.save(attendance);
        }

    }

    // 출석하지 않은 학생들을 결석 처리
    public void markAbsentForUnattendedStudents() {

        LocalDate today = LocalDate.now();
        // 오늘 출석한 학생들의 studentId
        List<Long> attendedUserIds = attendanceRepository.findUserIdsByDate(today);

        // 모든 학생의 studentId
        List<User> allUsers = userRepository.findAll();

        // 출석하지 않은 학생들을 결석 처리
        for (User user : allUsers) {
            if (!attendedUserIds.contains(user.getUserId())) {
                Attendance attendance = new Attendance();
                attendance.setUser(user);
                attendance.setAttendance(null);
                attendanceRepository.save(attendance);
            }
        }
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }
}

