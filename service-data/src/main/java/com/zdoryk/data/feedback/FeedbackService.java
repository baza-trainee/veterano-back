package com.zdoryk.data.feedback;

import com.zdoryk.data.dto.FeedbackDto;
import com.zdoryk.data.exception.NotFoundException;
import com.zdoryk.data.exception.NotValidFieldException;
import com.zdoryk.data.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void saveFeedback(FeedbackDto feedbackDto) {

        if(feedbackDto.getEmail() != null){
            if(Utils.isValidEmail(feedbackDto.getEmail())){
                throw  new NotValidFieldException("Email is not valid");
            }
        }

        Feedback feedback = Feedback.builder()
                .name(feedbackDto.getName())
                .message(feedbackDto.getMessage())
                .email(feedbackDto.getEmail())
                .build();

        feedbackRepository.save(feedback);
    }


    public List<Feedback> getAllFeedbacks(){
        return feedbackRepository.findAll();
    }

    public void deleteFeedback(Long id){
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Feedback with this id does not exist"));

        feedbackRepository.delete(feedback);
    }


}
