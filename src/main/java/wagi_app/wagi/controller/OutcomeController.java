package wagi_app.wagi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wagi_app.wagi.DTO.OutcomeCreateDto;
import wagi_app.wagi.DTO.OutcomeUpdateDto;
import wagi_app.wagi.entity.Outcome;
import wagi_app.wagi.service.OutcomeService;
import wagi_app.wagi.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/outcome")
@RequiredArgsConstructor
public class OutcomeController {
    private final OutcomeService outcomeService;
    private final UserService userService;

    // 메인 페이지 - 카테고리별 결과물 표시
    @GetMapping("")
    public String outcomeMain(Model model) {
        Map<String, List<Outcome>> categorizedOutcomes = new HashMap<>();
        List<Outcome> allOutcomes = outcomeService.getAllOutcomes();

        for (Outcome outcome : allOutcomes) {
            categorizedOutcomes.computeIfAbsent(outcome.getCategory(), k -> new ArrayList<>())
                    .add(outcome);
        }

        model.addAttribute("webOutcomes", categorizedOutcomes.getOrDefault("Web", new ArrayList<>()));
        model.addAttribute("appOutcomes", categorizedOutcomes.getOrDefault("App", new ArrayList<>()));
        model.addAttribute("gameOutcomes", categorizedOutcomes.getOrDefault("Game", new ArrayList<>()));

        return "outcome/outcome";
    }

    // 카테고리별 상세 페이지
    @GetMapping("/{category}/{id}")
    public String outcomeDetail(@PathVariable String category,
                                @PathVariable Long id,
                                Model model) {
        Outcome outcome = outcomeService.getOutcomeById(id);

        if (!outcome.getCategory().equalsIgnoreCase(category)) {
            return "redirect:/outcome";
        }

        model.addAttribute("outcome", outcome);
        model.addAttribute("category", category);

        return "outcome/outcome02_" + category.toLowerCase();
    }

    // 카테고리별 최신 결과물 페이지
    @GetMapping("/{category}")
    public String outcomeByCategory(@PathVariable String category, Model model) {
        String normalizedCategory = category.substring(0, 1).toUpperCase() +
                category.substring(1).toLowerCase();

        List<Outcome> outcomes = outcomeService.getOutcomesByCategory(normalizedCategory);

        if (outcomes.isEmpty()) {
            return "redirect:/outcome";
        }

        Outcome latestOutcome = outcomes.get(0);
        model.addAttribute("outcome", latestOutcome);
        model.addAttribute("category", normalizedCategory);

        return "outcome/outcome02_" + category.toLowerCase();
    }

    // 관리자 - 결과물 생성 페이지
    @GetMapping("/manager/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createOutcomeForm(Model model) {
        model.addAttribute("outcomeCreateDto", new OutcomeCreateDto());
        return "outcome/outcome03";
    }

    // 관리자 - 결과물 생성 처리
    @PostMapping("/manager/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createOutcome(@Valid @ModelAttribute OutcomeCreateDto outcomeCreateDto,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("로그인한 사용자가 없습니다.");
        }

        String userId = authentication.getName();
        outcomeCreateDto.setImageFile(imageFile);

        outcomeService.createOutcome(outcomeCreateDto, userId);
        return "redirect:/outcome";
    }

    // 관리자 - 결과물 수정 페이지
    @GetMapping("/manager/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editOutcomeForm(@PathVariable Long id, Model model) {
        Outcome outcome = outcomeService.getOutcomeById(id);
        model.addAttribute("outcome", outcome);
        return "outcome/outcome03_" + outcome.getCategory().toLowerCase();
    }

    // 관리자 - 결과물 수정 처리
    @PostMapping("/manager/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateOutcome(@PathVariable Long id,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("category") String category,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                Authentication authentication) throws IOException {
        String userId = authentication.getName();

        OutcomeUpdateDto outcomeUpdateDto = new OutcomeUpdateDto();
        outcomeUpdateDto.setTitle(title);
        outcomeUpdateDto.setContent(content);
        outcomeUpdateDto.setCategory(category);  // 카테고리 설정 추가
        outcomeUpdateDto.setImageFile(imageFile);

        outcomeService.updateOutcome(id, outcomeUpdateDto, userId);
        return "redirect:/outcome";
    }

    // 관리자 - 결과물 삭제
    @PostMapping("/manager/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteOutcome(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인한 사용자가 없습니다.");
        }

        String userId = authentication.getName();
        outcomeService.deleteOutcome(id, userId);
        return "redirect:/outcome";
    }
}