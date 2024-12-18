package wagi_app.wagi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wagi_app.wagi.DTO.NoticeCreateDto;
import wagi_app.wagi.DTO.NoticeUpdateDto;
import wagi_app.wagi.entity.Notice;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.NoticeRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class NoticeService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }
    // 공지사항 생성 (이미지 저장)
    public Notice createNotice(NoticeCreateDto dto, User createdBy) throws IOException {
        String imagePath = saveImage(dto.getImageFile());
        Notice notice = Notice.from(dto, createdBy, imagePath);
        return noticeRepository.save(notice);
    }
    // 모든 공지사항 조회
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    // ID로 공지사항 조회
    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));
    }

    // 공지사항 수정 (이미지 수정)
    public Notice updateNotice(Long id, NoticeUpdateDto dto, User updatedBy) throws IOException {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!notice.getCreatedBy().equals(updatedBy)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        // 기존 이미지 삭제 후 새 이미지 저장
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            deleteImage(notice.getImagePath());
            String imagePath = saveImage(dto.getImageFile());
            notice.setImagePath(imagePath);
        }

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        return noticeRepository.save(notice);
    }

    // 공지사항 삭제 (이미지 삭제)
    public void deleteNotice(Long id, User deletedBy) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!notice.getCreatedBy().equals(deletedBy)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        // 이미지 파일 삭제
        deleteImage(notice.getImagePath());

        noticeRepository.delete(notice);
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path directory = Paths.get(uploadDir);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory); // 폴더가 없으면 생성
        }

        Path filePath = directory.resolve(fileName);
        imageFile.transferTo(filePath.toFile());
        return "/uploads/img/" + fileName;
    }

    private void deleteImage(String imagePath) {
        if (imagePath != null) {
            File file = new File(uploadDir + "/" + imagePath.substring(imagePath.lastIndexOf("/") + 1));
            if (file.exists()) {
                file.delete();
            }
        }
    }
//
//    // 제목으로 공지사항 조회
//    public List<Notice> getNoticesByTitle(String title) {
//        return noticeRepository.findByTitleContaining(title); // 제목에 포함된 글자 찾기
//    }
//
//    // ID로 공지사항 조회
//    public Optional<Notice> getNoticeById(Long id) {
//        return noticeRepository.findById(id);
//    }
//
//    // 모든 공지사항 조회
//    public List<Notice> getAllNotices() {
//        return noticeRepository.findAll();
//    }
//
//    // 공지사항 수정 (제목으로 조회 후 수정)
//    public Notice updateNoticeByTitle(String oldTitle, NoticeUpdateDto noticeUpdateDto) {
//        List<Notice> notices = noticeRepository.findByTitleContaining(oldTitle);
//        if (notices.isEmpty()) {
//            throw new EntityNotFoundException("공지사항을 찾을 수 없습니다.");
//        }
//
//        Notice notice = notices.get(0); // 일치하는 첫 번째 공지사항을 수정
//        notice.setTitle(noticeUpdateDto.getTitle());
//        notice.setContent(noticeUpdateDto.getContent());
//        return noticeRepository.save(notice);
//    }
//
//    // 공지사항 삭제 (제목으로 조회 후 삭제)
//    public void deleteNoticeByTitle(String title) {
//        List<Notice> notices = noticeRepository.findByTitleContaining(title);
//        if (notices.isEmpty()) {
//            throw new EntityNotFoundException("공지사항을 찾을 수 없습니다.");
//        }
//        noticeRepository.delete(notices.get(0)); // 일치하는 첫 번째 공지사항 삭제
//    }

}
