package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import wagi_app.wagi.DTO.UserCreateDto;
import wagi_app.wagi.DTO.UserDto;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.AdmittedUsersRepository;
import wagi_app.wagi.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public List<UserDto> getUserList() {
        List <UserDto> members = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            members.add(UserDto.builder()
                            .studentId(user.getStudentId())
                            .role(user.getRole())
                            .build());
        }
        return members;
    }

    public void updateRole(UserDto userDto) throws  Exception{
        Optional<User> userOptional = userRepository.findByStudentId(userDto.getStudentId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(userDto.getRole()); // 권한 변경
            userRepository.save(user); // db 업데이트
        } else {
           throw new IllegalArgumentException();
        }
    }
    public UserDto getUserInfo() {
        // 로그인한 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("로그인한 사용자가 없습니다.");
        }

        // 로그인한 사용자의 userId 가져오기
        String userId = authentication.getName();
        // userId로 사용자 찾아서 학번, 이름 반환
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return UserDto.builder()
                    .username(user.getName())
                    .studentId(user.getStudentId())
                    .role(user.getRole())
                    .build();
        } else {
            throw new IllegalArgumentException();
        }

    }

}
