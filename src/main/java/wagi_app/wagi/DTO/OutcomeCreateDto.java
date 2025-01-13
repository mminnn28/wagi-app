package wagi_app.wagi.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class OutcomeCreateDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private MultipartFile imageFile;

    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;
}
