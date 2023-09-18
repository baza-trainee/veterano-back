package com.zdoryk.data.url;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "url controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;


    @GetMapping("redirect/{url}")
    public ResponseEntity<Void> redirectToOriginUrl(
            @PathVariable("url") UUID id
            ){
        return ResponseEntity
                .status(302)
                .location(urlService.redirectById(id))
                .build();
    }
}
