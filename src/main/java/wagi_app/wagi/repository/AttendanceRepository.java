package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import wagi_app.wagi.entity.Attendance;

@EnableJpaRepositories
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

}
