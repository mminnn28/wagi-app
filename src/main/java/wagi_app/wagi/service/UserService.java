package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wagi_app.wagi.DTO.UserCreateDto;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 저장
    public void saveUser(UserCreateDto userCreateDto) {
        User user = User.createUser(userCreateDto, passwordEncoder);
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("아이디와 해당하는 회원을 찾지 못했습니다. : " + userId));

        return user;
    }

}
