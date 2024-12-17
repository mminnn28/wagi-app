package wagi_app.wagi.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import wagi_app.wagi.DTO.UserCreateDto;
import lombok.*;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String userId;

    @NotNull
    private String username;

    @NotNull
    @Column(unique = true)
    private String studentId;

    @NotNull
    private String major;

    @NotNull
    private String password;

    private String role;

    public static User createUser(UserCreateDto userCreateDto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUserId(userCreateDto.getUserId());
        user.setUsername(userCreateDto.getUsername());
        user.setStudentId(userCreateDto.getStudentId());
        user.setMajor(userCreateDto.getMajor());
        String password = passwordEncoder.encode(userCreateDto.getPassword());
        user.setPassword(password);
        user.setRole("ROLE_USER");
        return user;
    }

    // UserDetails 인터페이스 메서드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 반환 ROLE_USER, etc...
        return Collections.singleton(() -> role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId; //userId로 로그인
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}