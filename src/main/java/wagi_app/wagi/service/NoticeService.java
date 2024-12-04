package wagi_app.wagi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wagi_app.wagi.entity.Notice;
import wagi_app.wagi.repository.NoticeRepository;

@Service
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public Notice createNotice(Notice notice) {
        return noticeRepository.save(notice);
    }
}

//    // 공지사항 생성
//    public Notice createNotice(Notice notice) {
//        return noticeRepository.save(notice);
//    }
//
//    // 공지사항 전체 조회
//    public List<Notice> getAllNotices() {
//        return noticeRepository.findAll();
//    }
//
//    // 공지사항 ID로 조회
//    public Optional<Notice> getNoticeById(Long id) {
//        return noticeRepository.findById(id);
//    }
//
//    // 공지사항 수정
//    public Notice updateNotice(Long id, Notice updatedNotice) {
//        return noticeRepository.findById(id)
//                .map(notice -> {
//                    notice.setTitle(updatedNotice.getTitle());
//                    notice.setContent(updatedNotice.getContent());
//                    return noticeRepository.save(notice);
//                })
//                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
//    }
//
//    // 공지사항 삭제
//    public void deleteNotice(Long id) {
//        noticeRepository.deleteById(id);
//    }

