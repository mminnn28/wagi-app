package wagi_app.wagi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wagi_app.wagi.DTO.OutcomeCreateDto;
import wagi_app.wagi.DTO.OutcomeUpdateDto;
import wagi_app.wagi.entity.Outcome;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.OutcomeRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class OutcomeService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final OutcomeRepository outcomeRepository;

    @Autowired
    public OutcomeService(OutcomeRepository outcomeRepository) {
        this.outcomeRepository = outcomeRepository;
    }
    // 공지사항 생성 (이미지 저장)
    public Outcome createOutcome(OutcomeCreateDto dto, User createdBy) throws IOException {
        String imagePath = saveImage(dto.getImageFile());
        Outcome outcome = Outcome.from(dto, createdBy, imagePath);
        return outcomeRepository.save(outcome);
    }
    // 모든 공지사항 조회
    public List<Outcome> getAllOutcomes() {
        return outcomeRepository.findAll();
    }

    // ID로 공지사항 조회
    public Outcome getOutcomeById(Long id) {
        return outcomeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공지사항을 찾을 수 없습니다."));
    }

    // 공지사항 수정 (이미지 수정)
    public Outcome updateOutcome(Long id, OutcomeUpdateDto dto, User updatedBy) throws IOException {
        Outcome outcome = outcomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!outcome.getCreatedBy().equals(updatedBy)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        // 기존 이미지 삭제 후 새 이미지 저장
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            deleteImage(outcome.getImagePath());
            String imagePath = saveImage(dto.getImageFile());
            outcome.setImagePath(imagePath);
        }

        outcome.setTitle(dto.getTitle());
        outcome.setContent(dto.getContent());
        return outcomeRepository.save(outcome);
    }

    // 공지사항 삭제 (이미지 삭제)
    public void deleteOutcome(Long id, User deletedBy) {
        Outcome outcome = outcomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!outcome.getCreatedBy().equals(deletedBy)) {
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
