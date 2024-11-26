package wagi_app.wagi.DTO;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserCreateDto {
    @NotEmpty(message = "ID를 입력해주세요.")
    //@Length(min=8, max=16, message = "아이디는 8자 이상, 16자 이하로 입력해주세요")
    private String userId;

    @NotEmpty(message = "이름을 입력해주세요.")
    private String username;

    @NotEmpty(message = "학번을 입력해주세요.")
    //@Length(min=8, max=8, message = "학번이 올바르지 않습니다.")
    private String studentId;

    @NotEmpty(message = "전공을 입력해주세요.")
    private String major;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    //@Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "비밀번호 확인란을 입력해주세요.")
    private String password2;

}
