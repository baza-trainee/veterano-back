package com.zdoryk.data.subscription;

import com.zdoryk.data.core.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @SneakyThrows
    @GetMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribeUser(
            @RequestParam("email")
            String email
    ){
        subscriptionService.unsubscribe(email);
        return ResponseEntity
                .status(302)
                .location(new URI("https://hyst.site"))
                .build();
    }
}
