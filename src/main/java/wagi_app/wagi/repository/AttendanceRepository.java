package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import wagi_app.wagi.entity.Attendance;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

//    // 특정 학생의 출석 기록 조회
//    List<Attendance> findByUserId(List<Long> userId);
}
