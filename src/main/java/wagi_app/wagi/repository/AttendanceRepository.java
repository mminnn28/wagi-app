package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import wagi_app.wagi.entity.Attendance;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

@EnableJpaRepositories
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

    // 출석한 학생들 찾기
    @Query("SELECT a.user.id FROM Attendance a WHERE a.createdDate = :createdDate AND a.attendance = '1'")
    List<Long> findUserIdsByDate(@Param("createdDate") LocalDate createdDate);



}
