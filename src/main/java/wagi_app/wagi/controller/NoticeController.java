package wagi_app.wagi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wagi_app.wagi.DTO.NoticeCreateDto;
import wagi_app.wagi.DTO.NoticeUpdateDto;
import wagi_app.wagi.entity.Notice;
import wagi_app.wagi.service.NoticeService;
import wagi_app.wagi.service.UserService;

import java.io.IOException;
import java.util.List;
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;
    // 생성자 주입
    public NoticeController(UserService userService, NoticeService noticeService) {
        this.userService = userService;
        this.noticeService = noticeService;
    }
    // Notice 목록 페이지
    @GetMapping
    public String getNoticeList(Model model) {
        List<Notice> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);
        return "notice/notice";
    }

    // 관리자용 Notice 생성 페이지
    @GetMapping("/manager/create")
    public String createNoticeForm(Model model) {
        model.addAttribute("noticeCreateDto", new NoticeCreateDto());
        return "notice/notice04_01";
    }

    @PostMapping("/manager/create")
    public String saveNotice(
            @ModelAttribute NoticeCreateDto noticeCreateDto,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("로그인한 사용자가 없습니다.");
        }

        // 로그인한 사용자의 아이디(userId) 가져오기
        String userId = authentication.getName();

        // NoticeCreateDto에 이미지 파일 추가
        noticeCreateDto.setImageFile(imageFile);

        // Notice 생성
        noticeService.createNotice(noticeCreateDto, userId);

        return "redirect:/notice";
    }

    // Notice 상세 페이지
    @GetMapping("/detail/{id}")
    public String getNoticeDetail(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "notice/notice03";
    }

    // 관리자용 Notice 수정 페이지
    @GetMapping("/manager/edit/{id}")
    public String editNoticeForm(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "notice/notice04_02";
    }

    // 관리자용 Notice 업데이트
    @PostMapping("/manager/edit/{id}")
    public String updateNotice(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Authentication authentication) throws IOException {

        String userId = authentication.getName();

        NoticeUpdateDto noticeUpdateDto = new NoticeUpdateDto();
        noticeUpdateDto.setTitle(title);
        noticeUpdateDto.setContent(content);
        noticeUpdateDto.setImageFile(imageFile);

        noticeService.updateNotice(id, noticeUpdateDto, userId);

        return "redirect:/notice";
    }

    // 관리자용 Notice 삭제
    @PostMapping("/manager/delete/{id}")
    public String deleteNotice(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인한 사용자가 없습니다.");
        }

        String userId = authentication.getName(); // 현재 로그인한 사용자의 ID를 가져옵니다
        noticeService.deleteNotice(id, userId);
        return "redirect:/notice";
    }
}