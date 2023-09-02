package com.zdoryk.data.searcher;

import com.zdoryk.data.dto.*;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.image.ImageService;
import com.zdoryk.data.info.InfoService;
import com.zdoryk.data.info.contact.Contact;
import com.zdoryk.data.info.partner.Partner;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/search")
public class SearcherController {

    private final SearchService searchService;
    private final ImageService imageService;
    private final InfoService infoService;


    @Operation(description = "Find enabled Cards by Queries parameters" +
            "all parameters are optional" +
            "if query is empty = get all cards "+
            "default page = 1, size = 10 "+
            "if you want to get by Location, please provide country and city "+
            "else your location will set to null")
    @GetMapping("card")
    public ResponseEntity<CardsPagination> getCardsByQueue(

            @RequestParam(
                    value = "q",
                    required = false,
                    defaultValue = ""
            )
            String queue,
            @RequestParam(
                    value = "country",
                    required = false,
                    defaultValue = ""
            ) LinkedList<String> countries,
            @RequestParam(
                    value = "city",
                    required = false,
                    defaultValue = ""
            ) LinkedList<String> cities,
            @RequestParam(
                    value = "category",
                    required = false
            ) List<String> categories,
            @RequestParam(
                    value = "page",
                    defaultValue = "1"
            ) Integer page,
            @RequestParam(
                    value = "size",
                    defaultValue = "10"
            ) Integer size

    ){
        return ResponseEntity.ok(searchService.findCardByAllQueries(
                queue,
                countries,
                cities,
                categories,
                page,
                size
        ));
    }

    @GetMapping("card/get")
    public ResponseEntity<CardDTO> getCardById(
            @RequestParam("id") Long id
    ){
        return ResponseEntity.ok(searchService.findCardById(id));
    }

    @Operation(description = "get image to the data by image id")
    @GetMapping("image/get")
    public ResponseEntity<byte[]> getImageById(@RequestParam("id") Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] imageBytes = imageService.getImageById(id);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @Operation(description = "Get all Categories")
    @GetMapping("all-categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(searchService.getAllCategories());
    }

    @GetMapping("all-countries")
    public ResponseEntity<List<LocationDTO>> getAllCountries(){
        return ResponseEntity.ok(searchService.getAllCounties());
    }

    @GetMapping("all-partners")
    public ResponseEntity<PartnersPagination> getAllPartners(
            @RequestParam(
                    value = "page",
                    defaultValue = "1"
            ) Integer page,
            @RequestParam(
                    value = "size",
                    defaultValue = "10"
            ) Integer size
    ){
        return ResponseEntity.ok(searchService.getAllPartners(page,size));
    }

    @GetMapping("all-contacts")
    public ResponseEntity<Contact> getContact(){
        return ResponseEntity.ok(infoService.getContact());
    }
}
