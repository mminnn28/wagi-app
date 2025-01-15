package wagi_app.wagi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import wagi_app.wagi.DTO.OutcomeCreateDto;
import wagi_app.wagi.DTO.OutcomeUpdateDto;
import wagi_app.wagi.entity.Outcome;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.OutcomeRepository;
import wagi_app.wagi.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OutcomeService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final OutcomeRepository outcomeRepository;
    private final UserRepository userRepository;

    // 아웃컴 생성 (이미지 저장)
    public Outcome createOutcome(OutcomeCreateDto dto, String userId) {
        try {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

            String imagePath = saveImage(dto.getImageFile());

            String category = dto.getCategory(); // Category 타입을 명시적으로 지정
            Outcome outcome = Outcome.from(dto, userId, imagePath, category);
            return outcomeRepository.save(outcome);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 중 오류가 발생했습니다.", e);
        }
    }


    public List<Outcome> getOutcomesByCategory(String category) {
        return outcomeRepository.findByCategory(category);
    }

    // 모든 아웃컴 조회
    public List<Outcome> getAllOutcomes() {
        return outcomeRepository.findAll();
    }

    // ID로 아웃컴 조회
    public Outcome getOutcomeById(Long id) {
        return outcomeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));
    }

    // 아웃컴 수정 (이미지 수정)
    public Outcome updateOutcome(Long id, OutcomeUpdateDto dto, String userId) throws IOException {
        Outcome outcome = getOutcomeById(id);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        // ADMIN 또는 MANAGER 역할을 가진 사용자는 모든 공지사항을 수정할 수 있도록 수정
        boolean isAdmin = user.getRole().equals("ADMIN");
        boolean isManager = user.getRole().equals("MANAGER");

        // 기존 데이터 업데이트
        outcome.setTitle(dto.getTitle());
        outcome.setContent(dto.getContent());
        outcome.setCategory(dto.getCategory());  // 카테고리 업데이트 추가

        // 이미지 파일이 있는 경우 처리
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            // 기존 이미지 삭제 로직
            if (outcome.getImagePath() != null) {
                // 실제 파일 삭제 로직
            }

            // 새 이미지 저장
            String imagePath = saveImage(dto.getImageFile());
            outcome.setImagePath(imagePath);
        }

        return outcomeRepository.save(outcome);
    }

    // 아웃컴 삭제 (이미지 삭제)
    public void deleteOutcome(Long id, String userId) {
        Outcome outcome = outcomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        // ADMIN 또는 MANAGER 역할을 가진 사용자는 모든 공지사항을 삭제할 수 있도록 수정
        boolean isAdmin = user.getRole().equals("ADMIN");
        boolean isManager = user.getRole().equals("MANAGER");

        if (!isAdmin && !isManager) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        // 이미지 파일 삭제
        deleteImage(outcome.getImagePath());

        outcomeRepository.delete(outcome);
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            // 업로드 디렉토리 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일명 생성 (원본 파일의 확장자 유지)
            String originalFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // 파일 저장
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // DB에 저장될 상대 경로 반환
            return "/uploads/" + fileName;  // 경로 형식 수정
        } catch (IOException e) {
            throw new IOException("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
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
