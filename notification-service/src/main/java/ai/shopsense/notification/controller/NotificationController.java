package ai.shopsense.notification.controller;

import ai.shopsense.notification.dto.CreateNotificationRequest;
import ai.shopsense.notification.dto.NotificationDto;
import ai.shopsense.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDto> send(@Valid @RequestBody CreateNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(notificationService.send(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }
}
