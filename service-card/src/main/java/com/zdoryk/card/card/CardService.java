package com.zdoryk.card.card;

import com.zdoryk.card.dto.CardSaveRequest;
import com.zdoryk.card.dto.CardWithImageIdDto;
import com.zdoryk.card.dto.LocationDTO;
import com.zdoryk.card.image.Image;
import com.zdoryk.card.image.ImageService;
import com.zdoryk.card.location.Location;
import com.zdoryk.card.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final LocationService locationService;
    private final ImageService imageService;

    @Transactional
    public void saveCard(CardSaveRequest cardSaveRequest) {
        Location location = locationService
                .getLocationByCountryAndCity(cardSaveRequest.getLocation())
                .orElseGet(() -> new Location(cardSaveRequest.getLocation().getCountry(), cardSaveRequest.getLocation().getCity()));

        Card card = Card.builder()
                .title(cardSaveRequest.getTitle())
                .description(cardSaveRequest.getDescription())
                .url(cardSaveRequest.getUrl())
                .category(Category.valueOf("TEST"))
                .build();

        List<Image> imageList = new ArrayList<>();
        Image image = Image.builder()
                .image(cardSaveRequest.getImage())
                .card(card)
                .build();
        imageList.add(image);

        if (location.getCardList() == null) {
            location.setCardList(new ArrayList<>());
        }

        location.getCardList().add(card);
        card.setLocation(location);
        card.setImageList(imageList);

        locationService.saveLocation(location);
        cardRepository.save(card);
        imageService.saveImage(image);
    }


    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public List<CardWithImageIdDto> getAllCardsWithImageId() {
        List<Card> cards = cardRepository.findAll();

        return cards.stream()
                .map(card -> new CardWithImageIdDto(
                        card.getDescription(),
                        card.getTitle(),
                        card.getUrl(),
                        card.getImageList().isEmpty() ? null : card.getImageList().get(0).getImageId(),
                        new LocationDTO(
                                card.getLocation().getCountry(),
                                card.getLocation().getCity()
                        )
                ))
                .collect(Collectors.toList());
    }
}
