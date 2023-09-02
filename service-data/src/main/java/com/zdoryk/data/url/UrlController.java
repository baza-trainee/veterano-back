package com.zdoryk.data.url;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "url controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;


    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectToOriginUrl(
            @RequestParam("id") UUID id
            ){
        return ResponseEntity
                .status(302)
                .location(urlService.redirectById(id))
                .build();
    }



}
