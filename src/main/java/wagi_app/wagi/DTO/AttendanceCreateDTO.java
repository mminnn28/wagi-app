package wagi_app.wagi.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceCreateDTO {

    private String userId; //user pk

    private Long attendanceId; //pk (DB 출결 정보 식별용)

    private String attendance; //출결 (1: 출석, 0: 지각, null: 결석)

    private String inputCode; //입력코드
}

