package com.zdoryk.data.feedback;

import com.zdoryk.data.core.ApiResponse;
import com.zdoryk.data.dto.FeedbackDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> saveFeedback(
            @Valid @RequestBody
            FeedbackDto feedbackDto){

        feedbackService.saveFeedback(feedbackDto);

        return ResponseEntity.ok().body(
                new ApiResponse(
                        HttpStatus.OK,
                        Map.of("Feedback","Saved"))
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteFeedBack(
            @RequestParam("id")
            Long id
    ){
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok().body(
                new ApiResponse(
                        HttpStatus.OK,
                        Map.of("Feedback","deleted")
                )
        );
    }


    @GetMapping
    public List<Feedback> getAllFeedbacks(){
        return feedbackService.getAllFeedbacks();
    }

}
