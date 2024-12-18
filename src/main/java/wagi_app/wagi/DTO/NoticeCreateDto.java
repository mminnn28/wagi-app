package wagi_app.wagi.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class NoticeCreateDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private MultipartFile imageFile;

//    public NoticeCreateDto() {}
//    public NoticeCreateDto(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }

}
