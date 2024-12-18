package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wagi_app.wagi.DTO.OutcomeCreateDto;
import wagi_app.wagi.DTO.OutcomeUpdateDto;
import wagi_app.wagi.entity.Outcome;
import wagi_app.wagi.entity.User;
import wagi_app.wagi.service.OutcomeService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/outcome")
@RequiredArgsConstructor
public class OutcomeController {
    private final OutcomeService outcomeService;

    // 모든 공지사항 조회
    @GetMapping
    public String getAllOutcomes(Model model) {
        List<Outcome> outcomes = outcomeService.getAllOutcomes();
        model.addAttribute("outcomes", outcomes);
        return "outcome/list";
    }

    // 공지사항 생성
    @PostMapping
    public String createOutcome(@ModelAttribute OutcomeCreateDto dto, @AuthenticationPrincipal User user)  throws IOException {
        outcomeService.createOutcome(dto, user);
        return "redirect:/outcome";
    }

    // 공지사항 수정 폼 표시
    @GetMapping("/{id}/edit")
    public String updateOutcomeForm(@PathVariable Long id, Model model) {
        Outcome outcome = outcomeService.getOutcomeById(id);
        model.addAttribute("outcome", outcome);
        model.addAttribute("outcomeUpdateDto", new OutcomeUpdateDto());
        return "outcome/editOutcome";
    }

    // 공지사항 수정
    @PostMapping("/{id}/edit")
    public String updateOutcome(@PathVariable Long id, @ModelAttribute OutcomeUpdateDto dto, @AuthenticationPrincipal User user) throws IOException {
        outcomeService.updateOutcome(id, dto, user);
        return "redirect:/outcome";
    }

    // 공지사항 삭제
    @PostMapping("/{id}/delete")
    public String deleteOutcome(@PathVariable Long id, @AuthenticationPrincipal User user) {
        outcomeService.deleteOutcome(id, user);
        return "redirect:/outcome";
    }

    // 공지사항 작성 폼을 보여주는 메서드
    @GetMapping("/write")
    public String createOutcomeForm(Model model) {
        model.addAttribute("outcomeCreateDto", new OutcomeCreateDto());
        return "outcome/addOutcome";
    }

}
