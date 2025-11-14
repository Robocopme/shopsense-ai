package ai.shopsense.price.controller;

import ai.shopsense.price.dto.CreatePriceWatchRequest;
import ai.shopsense.price.dto.PriceUpdateRequest;
import ai.shopsense.price.dto.PriceWatchDto;
import ai.shopsense.price.service.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @PostMapping("/watch")
    public ResponseEntity<PriceWatchDto> create(@Valid @RequestBody CreatePriceWatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.createWatch(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceWatchDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(priceService.findById(id));
    }

    @PostMapping("/{id}/events")
    public ResponseEntity<PriceWatchDto> record(@PathVariable UUID id, @Valid @RequestBody PriceUpdateRequest request) {
        return ResponseEntity.ok(priceService.recordPrice(id, request));
    }

    @GetMapping("/due")
    public List<PriceWatchDto> due() {
        return priceService.dueForCheck();
    }
}
