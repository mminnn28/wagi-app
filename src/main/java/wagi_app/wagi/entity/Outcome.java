package wagi_app.wagi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import wagi_app.wagi.DTO.OutcomeCreateDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class Outcome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outcome_id") // 컬럼 이름 설정
    private Long id;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "userId")
    private User createdBy; // Users 테이블 참조

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String imagePath; // 이미지 경로 저장 필드

    public static Outcome from(OutcomeCreateDto dto, User createdBy, String imagePath) {
        Outcome outcome = new Outcome();
        outcome.setTitle(dto.getTitle());
        outcome.setContent(dto.getContent());
        outcome.setCreatedBy(createdBy);
        outcome.setImagePath(imagePath);
        return outcome;
    }

}
