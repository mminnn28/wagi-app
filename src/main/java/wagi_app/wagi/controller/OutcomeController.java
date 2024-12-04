package wagi_app.wagi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wagi_app.wagi.entity.Outcome;
import wagi_app.wagi.service.OutcomeService;

@Controller
@RequestMapping("/outcome")
@RequiredArgsConstructor
public class OutcomeController {
    private final OutcomeService outcomeService;

    // 공지사항 작성 폼을 보여주는 메서드
    @GetMapping("/new")
    public String createOutcomeForm(Model model) {
        model.addAttribute("outcome", new Outcome());
        return "outcome/write";
    }

    // 공지사항을 저장하는 메서드
    @PostMapping
    public String createOutcome(@ModelAttribute Outcome outcome) {
        outcomeService.createOutcome(outcome);
        return "redirect:/outcome";  // 저장 후 목록 페이지로 리다이렉트
    }
}
