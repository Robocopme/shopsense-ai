package ai.shopsense.review.controller;

import ai.shopsense.review.dto.CreateReviewAnalysisRequest;
import ai.shopsense.review.dto.ReviewDto;
import ai.shopsense.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/analyze")
    public ResponseEntity<ReviewDto> analyze(@Valid @RequestBody CreateReviewAnalysisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.analyze(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ReviewDto> latest(@PathVariable UUID productId) {
        return ResponseEntity.ok(reviewService.latest(productId));
    }
}
