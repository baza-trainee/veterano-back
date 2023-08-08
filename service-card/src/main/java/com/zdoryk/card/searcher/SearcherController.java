package com.zdoryk.card.searcher;

import com.zdoryk.card.card.Card;
import com.zdoryk.card.dto.CardWithImageIdDto;
import com.zdoryk.card.dto.CardWithOutLocation;
import com.zdoryk.card.location.Location;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<List<CardWithImageIdDto>> getAllCards(){
        return ResponseEntity.ok(searchService.getAllCards());
    }

    @GetMapping("/card/location")
    private ResponseEntity<List<CardWithOutLocation>> getCardByLocation(
            @RequestParam("country") String country,
            @RequestParam("city") String city
    ){
        return ResponseEntity.ok(searchService.findByLocation(country,city));
    }



}
