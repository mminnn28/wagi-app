package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wagi_app.wagi.entity.Notice;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findById(Long id); // 기존의 ID로 조회
    List<Notice> findByTitleContaining(String title); // 제목에 글자가 포함된 공지사항 조회
}