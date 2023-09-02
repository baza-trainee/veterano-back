package com.zdoryk.data.subscription;

import com.zdoryk.RabbitMQRunner;
import com.zdoryk.data.card.Card;
import com.zdoryk.data.exception.NotFoundException;
import com.zdoryk.data.exception.NotValidFieldException;
import com.zdoryk.data.exception.ResourceExistsException;
import com.zdoryk.data.utils.Utils;
import com.zdoryk.util.CardToSendEmail;
import com.zdoryk.util.ConsumerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final RabbitMQRunner rabbitMQRunner;
    @Transactional
    public void subscribe(Subscription subscriptionToSave){

        if(Utils.isValidEmail(subscriptionToSave.getEmail())){
            throw  new NotValidFieldException("Email is not valid");
        }

        Optional<Subscription> optionalSubscription = subscriptionRepository
                .findSubscriptionByEmail(subscriptionToSave.getEmail());

        if(optionalSubscription.isPresent()){
            throw new ResourceExistsException("subscription already exist");
        }


        Subscription subscription = Subscription.builder()
                .email(subscriptionToSave.getEmail())
                .name(subscriptionToSave.getName())
                .build();

        rabbitMQRunner.sendSubscribeConfirmation(
                new ConsumerDto(
                        subscription.getEmail(),
                        subscription.getName()
                )
        );
        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(String email){
        Subscription subscription = subscriptionRepository
                .findSubscriptionByEmail(email)
                .orElseThrow(() -> new NotFoundException("subscription does not exist"));

        subscriptionRepository.delete(subscription);
    }

    @Async
    @Transactional
    public void notifyAllSubscriberAboutNewCards(List<Card> cards){
        List<ConsumerDto> consumers = subscriptionRepository.findAll()
                        .stream()
                        .map(sub -> new ConsumerDto(
                            sub.getEmail(),
                            sub.getName()
                        )).toList();
        List<CardToSendEmail> cardToSendEmails = cards.stream()
                        .map(card -> new CardToSendEmail(
                                card.getDescription(),
                                card.getTitle(),
                                card.getUrl().getUrl(),
                                card.getDescription(),
                                card.getLocation().getCountry(),
                                card.getLocation().getCity()
                        )).toList();

        rabbitMQRunner.sendNewCardsToSubscribers(consumers,cardToSendEmails);
    }


    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepository.findAll();
    }




}
