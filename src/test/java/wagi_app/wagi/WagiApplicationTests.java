package wagi_app.wagi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.NoticeRepository;
import wagi_app.wagi.service.NoticeService;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(properties = "file.upload-dir=src/test/resources/img")
public class WagiApplicationTests {

	@InjectMocks
	private NoticeService noticeService;

	@Mock
	private NoticeRepository noticeRepository;

	@Mock
	private User mockUser;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(noticeService, "uploadDir", "src/test/resources/img");
	}

}
