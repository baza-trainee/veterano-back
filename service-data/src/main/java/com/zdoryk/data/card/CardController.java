package com.zdoryk.data.card;

import com.zdoryk.data.core.ApiResponse;
import com.zdoryk.data.dto.CardDTO;
import com.zdoryk.data.dto.CardSaveRequest;
import com.zdoryk.data.dto.CardsPagination;
import com.zdoryk.data.dto.UpdateCardDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Controller for cards", description = "provides with JWT Token")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Save a Card", description = "Returns a product as per the id")
    @PostMapping("add")
    public ResponseEntity<String> saveCard(
          @Valid @RequestBody
          CardSaveRequest cardSaveRequest
    ){
       cardService.saveCard(cardSaveRequest);
       return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update existing Card")
    @PatchMapping("update")
    public ResponseEntity<String> updateCard(
           @RequestBody UpdateCardDTO cardDTO
    ){
       cardService.updateCard(cardDTO);
       return ResponseEntity.ok().build();
    }


    @DeleteMapping("delete")
    public ResponseEntity<String> deleteCardById(
           @RequestParam("id") Long id
    ){
       cardService.deleteCardById(id);
       return ResponseEntity.ok().build();
    }

    @Operation(summary = "getting card by id")
    @GetMapping("get")
    public ResponseEntity<CardDTO> getCardById(
           @RequestParam("id") Long id
    ){
       return ResponseEntity.ok(cardService.getCardById(id));
    }

    @Operation(description = "get all cards")
    @GetMapping("get-all")
    public ResponseEntity<CardsPagination> getAll(
            @RequestParam(
                    value = "page",
                    defaultValue = "1"
            ) Integer page,
            @RequestParam(
                    value = "size",
                    defaultValue = "10"
            ) Integer size
    ){
        return ResponseEntity.ok(cardService.getAllCardsForAdmin(page,size));
    }

}
