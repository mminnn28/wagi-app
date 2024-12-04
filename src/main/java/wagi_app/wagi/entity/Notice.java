package wagi_app.wagi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Notices") // 테이블 이름 설정
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id") // 컬럼 이름 설정
    private Long id;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

//    @ManyToOne
//    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
//    private User createdBy; // Users 테이블 참조
//
//    @Column(name = "created_at", updatable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO) // 자동으로 생성되는 타임스탬프
//    private LocalDateTime createdAt;

//    // 기본 생성자
//    public Notice() {}
//
//    // 생성자, getter 및 setter
//    public Notice(String title, String content) {
//        this.title = title;
//        this.content = content;
//        //       this.createdBy = createdBy;
////        this.createdAt = LocalDateTime.now(); // 현재 시간 설정
//    }

}
