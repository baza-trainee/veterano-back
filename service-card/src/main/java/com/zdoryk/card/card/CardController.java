package com.zdoryk.card.card;

import com.zdoryk.card.core.ApiResponse;
import com.zdoryk.card.dto.CardSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;


   @PostMapping("/add")
   public ResponseEntity<ApiResponse> saveCard(
           @Valid @RequestBody
           CardSaveRequest cardSaveRequest){

       cardService.saveCard(cardSaveRequest);
       return ResponseEntity.ok(new ApiResponse(
               HttpStatus.OK,
               Map.of("card","card saved")
       ));
   }

    @GetMapping("/get")
    public List<Card> getAll(){
        return cardService.getAllCards();
    }

}
