package wagi_app.wagi.Manager;

import org.springframework.stereotype.Component;
import wagi_app.wagi.service.AttendanceService;

@Component
public class AttendanceCodeManager {
    private String attendanceCode;
    private long expirationTime; // 유효 시간 (밀리초 단위)

    // 출석 코드 생성
    public void generateAttendanceCode(String code) {
        this.attendanceCode = code;
        this.expirationTime = System.currentTimeMillis() + 60000; // 1분 유효
        System.out.println(code);
    }

    // 유효한 출석 코드 반환, 만료된 경우 null 반환
    public String getAttendanceCode() {
        if (System.currentTimeMillis() > expirationTime) {
            return null; // 인증 코드 만료
        }
        return attendanceCode;
    }

    // 인증 코드 만료 확인
    public boolean isCodeExpired() {
        return System.currentTimeMillis() > expirationTime;
    }

    // 만료된 코드가 있으면 결석 처리
    public void handleExpiredCode(AttendanceService attendanceService) {
        if (isCodeExpired()) {
            attendanceService.markAbsentForUnattendedStudents();  // 결석 처리
            clearCode();  // 만료된 코드 초기화
        }
    }

    // 코드 초기화
    public void clearCode() {
        this.attendanceCode = null;
        this.expirationTime = 0;
    }
}

