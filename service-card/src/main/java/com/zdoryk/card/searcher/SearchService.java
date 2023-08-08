package com.zdoryk.card.searcher;

import com.zdoryk.card.card.Card;
import com.zdoryk.card.card.CardService;
import com.zdoryk.card.dto.CardWithImageIdDto;
import com.zdoryk.card.dto.CardWithOutLocation;
import com.zdoryk.card.dto.LocationDTO;
import com.zdoryk.card.dto.LocationFindRequest;
import com.zdoryk.card.exception.NotFoundException;
import com.zdoryk.card.image.ImageService;
import com.zdoryk.card.location.Location;
import com.zdoryk.card.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final CardService cardService;
    private final ImageService imageService;
    private final LocationService locationService;


    public List<CardWithImageIdDto> getAllCards(){
        return cardService.getAllCardsWithImageId();
    }


    public List<CardWithOutLocation> findByLocation(String country, String city) {
        LocationFindRequest locationDTO = new LocationFindRequest(
                city,
                country
        );
        Location location = locationService.getLocationByCountryAndCity(locationDTO)
                .orElseThrow(() -> new NotFoundException("By this location no available cards"));

        List<Card> cards = location.getCardList();
        if(cards.isEmpty()){
            throw  new NotFoundException("By this location no available cards");
        }

        return  cards.stream().map(card -> new CardWithOutLocation(
                card.getDescription(),
                card.getTitle(),
                card.getUrl(),
                card.getImageList().isEmpty() ? null : card.getImageList().get(0).getImageId()
        )).collect(Collectors.toList());

    }
}
