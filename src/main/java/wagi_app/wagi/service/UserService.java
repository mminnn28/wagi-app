package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        // 아이디 중복 체크
        if (userRepository.existsByUserId(userCreateDto.getUserId())) {
            throw new Exception("이미 존재하는 아이디입니다.");
        } else if (userRepository.existsByStudentId(userCreateDto.getStudentId())) {
            throw new Exception("이미 가입된 학번입니다.");
        } else if (!admittedUsersRepository.existsByAdmittedId(userCreateDto.getStudentId())) {
            throw new Exception("가입할 수 없는 아이디입니다.");
        }

        User user = User.createUser(userCreateDto, passwordEncoder);
        userRepository.saveAndFlush(user);
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
