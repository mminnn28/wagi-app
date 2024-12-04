package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.entity.Notice;
import wagi_app.wagi.service.NoticeService;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 작성 폼을 보여주는 메서드
    @GetMapping("/new")
    public String createNoticeForm(Model model) {
        model.addAttribute("notice", new Notice());
        return "addNotice";
    }

    // 공지사항을 저장하는 메서드
    @PostMapping
    public String createNotice(@ModelAttribute Notice notice) {
        noticeService.createNotice(notice);
        return "redirect:/notices";  // 저장 후 목록 페이지로 리다이렉트
    }

     //공지사항 삭제
     @DeleteMapping("/{id}")
     public String deleteNotice(@PathVariable Long id) {
         noticeService.deleteNotice(id);
         return "redirect:/notices";
     }

    // 공지사항 수정 폼 보기
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNoticeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
        model.addAttribute("notice", notice);
        return "notices/edit-form";
    }

    // 공지사항 수정 처리
    @PutMapping("/{id}")
    public String updateNotice(@PathVariable Long id,
                               @ModelAttribute Notice noticeDetails) {
        Notice existingNotice = noticeService.getNoticeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));

        // 기존 공지사항 정보 업데이트
        existingNotice.setTitle(noticeDetails.getTitle());
        existingNotice.setContent(noticeDetails.getContent());
        // 필요한 다른 필드들도 업데이트

        noticeService.updateNotice(existingNotice);
        return "redirect:/notices";
    }

}

//    // 공지사항 리스트 조회
//    @GetMapping("/list")
//    public String getAllNotices(Model model) {
//        model.addAttribute("notices", noticeService.getAllNotices()); // 공지사항 리스트 모델에 추가
//        return "noticeList"; // noticeList.html로 렌더링
//    }

//    // 공지사항 목록 페이지
//    @GetMapping
//    public String getAllNotices(Model model) {
//        List<Notice> notices = noticeService.getAllNotices();
//        model.addAttribute("notices", notices);
//        return "notices/list";
//    }
//
//    // 공지사항 작성 폼
//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("notice", new Notice());
//        return "notices/form";
//    }
//
//    // 공지사항 생성
//    @PostMapping
//    public String createNotice(@ModelAttribute Notice notice) {
//        noticeService.createNotice(notice);
//        return "redirect:/notices";
//    }
//
//    // 공지사항 상세 조회
//    @GetMapping("/{id}")
//    public String getNoticeById(@PathVariable Long id, Model model) {
//        Notice notice = noticeService.getNoticeById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
//        model.addAttribute("notice", notice);
//        return "notices/detail";
//    }
//
//
//
//    // 공지사항 업데이트
//    @PostMapping("/{id}/edit")
//    public String updateNotice(@PathVariable Long id, @ModelAttribute Notice notice) {
//        noticeService.updateNotice(id, notice);
//        return "redirect:/notices/" + id;
//    }
//
//    // 공지사항 삭제
//    @PostMapping("/{id}/delete")
//    public String deleteNotice(@PathVariable Long id) {
//        noticeService.deleteNotice(id);
//        return "redirect:/notices";
//    }


