package ai.shopsense.analytics.controller;

import ai.shopsense.analytics.dto.AnalyticsEventDto;
import ai.shopsense.analytics.dto.CreateAnalyticsEventRequest;
import ai.shopsense.analytics.service.AnalyticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping
    public ResponseEntity<AnalyticsEventDto> record(@Valid @RequestBody CreateAnalyticsEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(analyticsService.record(request));
    }

    @GetMapping
    public List<AnalyticsEventDto> between(@RequestParam OffsetDateTime from, @RequestParam OffsetDateTime to) {
        return analyticsService.between(from, to);
    }
}
