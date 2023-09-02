package com.zdoryk.data.subscription;

import com.zdoryk.data.core.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<ApiResponse> subscribeUser(
            @Valid @RequestBody
            Subscription subscription
    ){

        subscriptionService.subscribe(subscription);

        return ResponseEntity.ok().body(
                new ApiResponse(
                        HttpStatus.OK,
                        Map.of("Sub","Done")
                )
        );
    }

    @GetMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribeUser(
            @RequestParam("email")
            String email
    ){
        subscriptionService.unsubscribe(email);
        return ResponseEntity.ok().build();
    }
}
