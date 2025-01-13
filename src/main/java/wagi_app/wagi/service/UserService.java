package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import wagi_app.wagi.DTO.UserCreateDto;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.AdmittedUsersRepository;
import wagi_app.wagi.repository.UserRepository;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdmittedUsersRepository admittedUsersRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원 가입
    @Transactional
    public void saveUser(UserCreateDto userCreateDto) throws Exception {
        // 회원 가입 유효 검증
        if (userRepository.existsByUserId(userCreateDto.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        } else if (userRepository.existsByStudentId(userCreateDto.getStudentId())) {
            throw new IllegalArgumentException("이미 가입된 학번입니다.");
        } else if (!admittedUsersRepository.existsByAdmittedId(userCreateDto.getStudentId())) {
            throw new IllegalArgumentException("가입할 수 없는 학번입니다.");
        }

        User user = User.createUser(userCreateDto, passwordEncoder);
        try {
            userRepository.saveAndFlush(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 중 오류가 발생했습니다.");
        }
    }

    // 로그인 시 인증된 사용자인지 확인
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

}
