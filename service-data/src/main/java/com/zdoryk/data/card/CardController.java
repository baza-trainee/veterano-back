package com.zdoryk.data.card;

import com.zdoryk.data.core.ApiResponse;
import com.zdoryk.data.dto.CardSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Controller for cards")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;


   @Operation(summary = "Save a Card", description = "Returns a product as per the id")
   @PostMapping("/add")
   public ResponseEntity<ApiResponse> saveCard(
           @Valid @RequestBody
           CardSaveRequest cardSaveRequest){

       cardService.saveCard(cardSaveRequest);
       return ResponseEntity.ok(new ApiResponse(
               HttpStatus.OK,
               Map.of("data","data saved")
       ));
   }

    @GetMapping("/get")
    public List<Card> getAll(){
        return cardService.getAllCards();
    }

}
