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




//    @PostMapping("/image")
//    public ResponseEntity<byte[]> getImageFromBase64(@RequestBody String base64Content) {
//        try {
//            byte[] imageBytes = Base64.getDecoder().decode(base64Content);
//
//            // You may want to validate the image data here before returning it.
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG); // Change this to the appropriate image type if needed
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
}
