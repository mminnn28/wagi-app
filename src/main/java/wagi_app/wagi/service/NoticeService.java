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
import wagi_app.wagi.repository.UserRepository;

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
    private final UserRepository userRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }
    // 공지사항 생성 (이미지 저장)
    public Notice createNotice(NoticeCreateDto dto, String userId) throws IOException {
        User createdBy = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        String imagePath = saveImage(dto.getImageFile());
        Notice notice = Notice.from(dto, createdBy.getUserId(), imagePath);
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
    public Notice updateNotice(Long id, NoticeUpdateDto dto, String userId) throws IOException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        // ADMIN 또는 MANAGER 역할을 가진 사용자는 모든 공지사항을 삭제할 수 있도록 수정
        boolean isAdmin = user.getRole().equals("ADMIN");
        boolean isManager = user.getRole().equals("MANAGER");

        if (!isAdmin && !isManager) {
            throw new SecurityException("삭제 권한이 없습니다.");
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

    public void deleteNotice(Long id, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        // ADMIN 또는 MANAGER 역할을 가진 사용자는 모든 공지사항을 삭제할 수 있도록 수정
        boolean isAdmin = user.getRole().equals("ADMIN");
        boolean isManager = user.getRole().equals("MANAGER");

        if (!isAdmin && !isManager) {
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

}
