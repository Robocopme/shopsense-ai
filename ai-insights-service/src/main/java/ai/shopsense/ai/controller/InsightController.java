package ai.shopsense.ai.controller;

import ai.shopsense.ai.dto.CreateInsightRequest;
import ai.shopsense.ai.dto.InsightDto;
import ai.shopsense.ai.service.InsightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;

    @PostMapping("/analyze")
    public ResponseEntity<InsightDto> analyze(@Valid @RequestBody CreateInsightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(insightService.generate(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InsightDto> latest(@PathVariable UUID productId) {
        return ResponseEntity.ok(insightService.latest(productId));
    }
}
