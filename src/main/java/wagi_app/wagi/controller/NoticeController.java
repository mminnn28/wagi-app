package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.NoticeCreateDto;
import wagi_app.wagi.DTO.NoticeUpdateDto;
import wagi_app.wagi.entity.Notice;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.service.NoticeService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 모든 공지사항 조회
    @GetMapping
    public String getAllNotices(Model model) {
        List<Notice> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);
        return "notice/list";
    }

    // 공지사항 생성
    @PostMapping
    public String createNotice(@ModelAttribute NoticeCreateDto dto, @AuthenticationPrincipal User user)  throws IOException {
        noticeService.createNotice(dto, user);
        return "redirect:/notice";
    }

    // 공지사항 수정 폼 표시
    @GetMapping("/{id}/edit")
    public String updateNoticeForm(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        model.addAttribute("noticeUpdateDto", new NoticeUpdateDto());
        return "notice/editNotice"; // templates/notice/editNotice.html
    }

    // 공지사항 수정
    @PostMapping("/{id}/edit")
    public String updateNotice(@PathVariable Long id, @ModelAttribute NoticeUpdateDto dto, @AuthenticationPrincipal User user) throws IOException {
        noticeService.updateNotice(id, dto, user);
        return "redirect:/notice";
    }

    // 공지사항 삭제
    @PostMapping("/{id}/delete")
    public String deleteNotice(@PathVariable Long id, @AuthenticationPrincipal User user) {
        noticeService.deleteNotice(id, user);
        return "redirect:/notice";
    }

    // 공지사항 작성 폼을 보여주는 메서드
    @GetMapping("/write")
    public String createNoticeForm(Model model) {
        model.addAttribute("noticeCreateDto", new NoticeCreateDto());
        return "notice/addNotice";
    }

}
