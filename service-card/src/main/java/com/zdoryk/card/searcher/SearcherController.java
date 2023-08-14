package com.zdoryk.card.searcher;

import com.zdoryk.card.dto.*;
import com.zdoryk.card.image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/search")
public class SearcherController {

    private final SearchService searchService;
    private final ImageService imageService;

    @Operation(description = "Get all Cards")
    @GetMapping
    public ResponseEntity<List<CardWithImageIdDto>> getAllCards(){
        return ResponseEntity.ok(searchService.getAllCards());
    }

    @Operation(description = "Get Card by Location")
    @GetMapping("/card/location")
    private ResponseEntity<List<CardWithOutLocation>> getCardsByLocation(
            @RequestParam("country") String country,
            @RequestParam("city") String city
    ){
        return ResponseEntity.ok(searchService.findByLocation(country,city));
    }

    @Operation(description = "get image to the card by image id")
    @GetMapping("image/get")
    public ResponseEntity<byte[]> getImageById(@RequestParam("id") Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] imageBytes = imageService.getImageById(id);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @Operation(description = "Get cards by category")
    @GetMapping("card/category")
    private ResponseEntity<List<CardWithoutCategoryWithImageIdDto>> getCardsByCategory(
            @RequestParam("category") String category
    ){
        return ResponseEntity.ok(searchService.findCardsByCategoryName(category));
    }
    @Operation(description = "Get Card by category and Location")
    @GetMapping("card/")
    private ResponseEntity<List<CardDTO>> getCardsByCategoryAndLocation(
            @RequestParam("category") String category,
            @RequestParam("country") String country,
            @RequestParam("city") String city
    ){
        return ResponseEntity.ok(searchService.findCardsByCategoryAndLocation(
                category,
                country,
                city
        ));
    }

    @Operation(description = "Get all Categories")
    @GetMapping("category")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(searchService.getAllCategories());
    }



}
