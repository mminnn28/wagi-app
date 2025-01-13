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

    @Column(name = "created_by", nullable = false)
    private String createdBy; // 작성자 아이디를 문자열로 저장

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String imagePath; // 이미지 경로 저장 필드

    @Column(name = "category", nullable = false)
    private String category; // 카테고리 필드 추가

    public static Outcome from(OutcomeCreateDto dto, String createdBy, String imagePath, String category) {
        Outcome outcome = new Outcome();
        outcome.setTitle(dto.getTitle());
        outcome.setContent(dto.getContent());
        outcome.setCreatedBy(createdBy);
        outcome.setImagePath(imagePath);
        outcome.setCategory(category);
        return outcome;
    }
}
