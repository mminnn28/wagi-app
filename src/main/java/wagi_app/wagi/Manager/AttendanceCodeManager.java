package wagi_app.wagi.Manager;

import org.springframework.stereotype.Component;

@Component
public class AttendanceCodeManager {
    private String attendanceCode;
    private long expirationTime; // 유효 시간

    public void generateAttendanceCode(String code) {
        this.attendanceCode = code;
        this.expirationTime = System.currentTimeMillis() + 60000; // 1분
    }

    public String getAttendanceCode() {
        if (System.currentTimeMillis() > expirationTime) {
            return null; // 인증 코드 만료
        }
        return attendanceCode;
    }
}
