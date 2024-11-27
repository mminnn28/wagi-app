package wagi_app.wagi.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "attendance_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId; //pk (DB 출결 정보 식별용)

    @NotNull
    private Long userId; //학번

    private String username; //이름

    private Integer attendance; //출결 (1: 출석, 0: 지각, null: 결석)

}
