package wagi_app.wagi.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto {
    @NotNull(message = "ID를 입력해주세요.")
    @NotEmpty(message = "ID는 비어 있을 수 없습니다.")
    private String userId;

    @NotNull(message = "이름을 입력해주세요.")
    @NotEmpty(message = "이름은 비어 있을 수 없습니다.")
    private String username;

    @NotNull(message = "학번을 입력해주세요.")
    @NotEmpty(message = "학번은 비어 있을 수 없습니다.")
    private String studentId;

    @NotNull(message = "전공을 입력해주세요.")
    @NotEmpty(message = "전공은 비어 있을 수 없습니다.")
    private String major;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @NotEmpty(message = "비밀번호는 비어 있을 수 없습니다.")
    private String password;

    @NotNull(message = "비밀번호 확인란을 입력해주세요.")
    @NotEmpty(message = "비밀번호 확인란은 비어 있을 수 없습니다.")
    private String password2;

}
