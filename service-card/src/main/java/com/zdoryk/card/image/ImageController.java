package com.zdoryk.card.image;

import com.zdoryk.card.dto.ImageDTO;
import com.zdoryk.card.dto.ImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;


    @PostMapping("/get")
    public ResponseEntity<byte[]> getImageById(
            @RequestBody
            ImageRequest imageRequest
    ){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] imageBytes = imageService.getImageById(imageRequest.getId());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


}
