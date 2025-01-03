package wagi_app.wagi.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AttendanceCreateDTO {

    private Long attendanceId; //pk (DB 출결 정보 식별용)

    private String userId; //학번

    private String username; //이름

    private Integer attendance; //출결 (1: 출석, 0: 지각, null: 결석)

    private int inputCode; //입력코드

}

