package wagi_app.wagi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import wagi_app.wagi.DTO.NoticeCreateDto;
import wagi_app.wagi.DTO.NoticeUpdateDto;
import wagi_app.wagi.entity.Notice;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.repository.NoticeRepository;
import wagi_app.wagi.service.NoticeService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

	// -----------------------------------------------
	// 1. 공지사항 생성 테스트
	// -----------------------------------------------
	@Test
	void testCreateNotice() throws IOException {
		// Arrange
		MockMultipartFile imageFile = new MockMultipartFile(
				"imageFile", "testImage.png", "image/png", "test content".getBytes()
		);

		NoticeCreateDto createDto = new NoticeCreateDto();
		createDto.setTitle("Test Title");
		createDto.setContent("Test Content");
		createDto.setImageFile(imageFile);

		Notice savedNotice = new Notice();
		savedNotice.setId(1L);
		savedNotice.setTitle("Test Title");
		savedNotice.setContent("Test Content");
		savedNotice.setImagePath("/img/testImage.png");
		savedNotice.setCreatedBy(mockUser);

		when(noticeRepository.save(any(Notice.class))).thenReturn(savedNotice);

		// Act
		Notice result = noticeService.createNotice(createDto, mockUser);

		// Assert
		assertNotNull(result);
		assertEquals("Test Title", result.getTitle());
		assertEquals("/img/testImage.png", result.getImagePath());
		verify(noticeRepository, times(1)).save(any(Notice.class));
	}

	// -----------------------------------------------
	// 2. 모든 공지사항 조회 테스트
	// -----------------------------------------------
	@Test
	void testGetAllNotices() {
		// Arrange
		Notice notice1 = new Notice();
		notice1.setId(1L);
		notice1.setTitle("Notice 1");
		notice1.setContent("Content 1");

		Notice notice2 = new Notice();
		notice2.setId(2L);
		notice2.setTitle("Notice 2");
		notice2.setContent("Content 2");

		when(noticeRepository.findAll()).thenReturn(Arrays.asList(notice1, notice2));

		// Act
		List<Notice> result = noticeService.getAllNotices();

		// Assert
		assertEquals(2, result.size());
		assertEquals("Notice 1", result.get(0).getTitle());
		verify(noticeRepository, times(1)).findAll();
	}
//	void testGetAllNotices() {
//		// Arrange
//		Notice notice1 = new Notice();
//		notice1.setTitle("Notice 1");
//		notice1.setContent("Content 1");
//
//		Notice notice2 = new Notice();
//		notice2.setTitle("Notice 2");
//		notice2.setContent("Content 2");
//
//		when(noticeRepository.findAll()).thenReturn(Arrays.asList(notice1, notice2));
//
//		// Act
//		List<Notice> result = noticeService.getAllNotices();
//
//		// Assert
//		assertEquals(2, result.size());
//		verify(noticeRepository, times(1)).findAll();
//	}


	// -----------------------------------------------
	// 3. ID로 공지사항 조회 테스트
	// -----------------------------------------------
	@Test
	void testGetNoticeById() {
		// Arrange
		Long noticeId = 1L;
		Notice mockNotice = new Notice();
		mockNotice.setId(noticeId);
		mockNotice.setTitle("Test Notice");

		when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(mockNotice));

		// Act
		Notice result = noticeService.getNoticeById(noticeId);

		// Assert
		assertNotNull(result);
		assertEquals("Test Notice", result.getTitle());
	}
//	void testGetNoticeById() {
//		// Arrange
//		Long noticeId = 1L;
//		Notice notice = new Notice();
//		notice.setId(noticeId);
//		notice.setTitle("Test Notice");
//
//		when(noticeRepository.findById(any())).thenReturn(Optional.of(new Notice()));
//
//		// Act
//		Notice result = noticeService.getNoticeById(noticeId);
//
//		// Assert
//		assertNotNull(result);
//		assertEquals(noticeId, result.getId());
//		assertEquals("Test Notice", result.getTitle());
//		verify(noticeRepository, times(1)).findById(noticeId);
//	}

	// -----------------------------------------------
	// 4. 공지사항 수정 테스트
	// -----------------------------------------------
	@Test
	void testUpdateNotice() throws IOException {
		// Arrange
		Long noticeId = 1L;

		NoticeUpdateDto updateDto = new NoticeUpdateDto();
		updateDto.setTitle("Updated Title");
		updateDto.setContent("Updated Content");

		MockMultipartFile newImage = new MockMultipartFile(
				"imageFile", "newImage.png", "image/png", "new image content".getBytes()
		);
		updateDto.setImageFile(newImage);

		Notice existingNotice = new Notice();
		existingNotice.setId(noticeId);
		existingNotice.setTitle("Old Title");
		existingNotice.setContent("Old Content");
		existingNotice.setCreatedBy(mockUser);
		existingNotice.setImagePath("/img/oldImage.png");

		when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));
		when(noticeRepository.save(any(Notice.class))).thenReturn(existingNotice);

		// Act
		Notice result = noticeService.updateNotice(noticeId, updateDto, mockUser);

		// Assert
		assertNotNull(result);
		assertEquals("Updated Title", result.getTitle());
		assertEquals("Updated Content", result.getContent());
		assertTrue(result.getImagePath().contains("newImage.png"));
		verify(noticeRepository, times(1)).save(existingNotice);
	}

	// -----------------------------------------------
	// 5. 공지사항 삭제 테스트
	// -----------------------------------------------
	@Test
	void testDeleteNotice() {
		// Arrange
		Long noticeId = 1L;

		Notice existingNotice = new Notice();
		existingNotice.setId(noticeId);
		existingNotice.setCreatedBy(mockUser);

		when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));
		doNothing().when(noticeRepository).delete(existingNotice);

		// Act
		assertDoesNotThrow(() -> noticeService.deleteNotice(noticeId, mockUser));

		// Assert
		verify(noticeRepository, times(1)).delete(existingNotice);
	}
}
