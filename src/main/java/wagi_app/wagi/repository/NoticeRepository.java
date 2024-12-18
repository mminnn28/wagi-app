package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wagi_app.wagi.entity.Notice;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findById(Long id); // 기존의 ID로 조회

}