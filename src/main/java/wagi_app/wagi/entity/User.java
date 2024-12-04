package wagi_app.wagi.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Users") // 테이블 이름 설정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // 컬럼 이름 설정
    private long userId;

    @Column(name = "username", length = 50, unique = true) // 고유 제약 조건 추가
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    // 기본 생성자
    public User() {}

    // 사용자 정의 생성자
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
