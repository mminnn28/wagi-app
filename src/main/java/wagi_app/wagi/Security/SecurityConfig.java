package wagi_app.wagi.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 출석 생성 코드 페이지 접근 권한
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // 글 작성 페이지 접근 권한
                        .requestMatchers("/notice/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/outcome/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/", "/login", "/signup").permitAll() // 인증되지 않은 사용자의 접근 가능 페이지
                        .requestMatchers("/", "/login", "/signup", "/makers").permitAll() // 인증되지 않은 사용자의 접근 가능 페이지
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // 정적 리소스 접근 허용
                        .requestMatchers("/notice").permitAll()
                        .requestMatchers("/notice/detail/**").permitAll()
                        .requestMatchers("/outcome").permitAll()// 상세 페이지도 허용
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("userId")
                )
                .logout(logout -> logout
                      .logoutUrl("/logout")
                      .logoutSuccessUrl("/")
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/login"))
                );
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }
}
