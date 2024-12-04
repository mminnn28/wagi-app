package wagi_app.wagi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Outcome") // 테이블 이름 설정
public class Outcome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outcome_id") // 컬럼 이름 설정
    private Long id;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    // 기본 생성자
    public Outcome() {}

    // 생성자, getter 및 setter
    public Outcome(String title, String content) {
        this.title = title;
        this.content = content;
        //       this.createdBy = createdBy;
//        this.createdAt = LocalDateTime.now(); // 현재 시간 설정
    }
}
