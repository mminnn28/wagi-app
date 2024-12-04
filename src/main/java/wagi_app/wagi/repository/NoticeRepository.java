package wagi_app.wagi.repository;
import wagi_app.wagi.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 기본 CRUD 메서드들이 자동으로 제공됨
}