package com.zdoryk.data.searcher;

import com.zdoryk.data.card.Card;
import com.zdoryk.data.card.CardService;
import com.zdoryk.data.category.CategoryService;
import com.zdoryk.data.dto.*;
import com.zdoryk.data.exception.NotFoundException;
import com.zdoryk.data.image.ImageService;
import com.zdoryk.data.location.Location;
import com.zdoryk.data.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final CardService cardService;
    private final ImageService imageService;
    private final LocationService locationService;
    private final CategoryService categoryService;



    public List<CardWithImageIdDto> getAllCards(int page, int size){
        return  cardService.getAllCardsWithImageId(page,size);
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
            throw new NotFoundException("By this location no available cards");
        }

        return cards
                .stream()
                .filter(Card::getIsEnabled)
                .map(card -> new CardWithOutLocation(
                card.getDescription(),
                card.getTitle(),
                card.getUrl(),
                card.getCategory().getCategoryName(),
                card.getPublication(),
                card.getImageList().isEmpty() ? null : card.getImageList().get(0).getImageId()
                ))
                .collect(Collectors.toList());

    }

    public List<CardWithoutCategoryWithImageIdDto> findCardsByCategoryName(String categoryRequest) {
        return cardService.getAllCards()
                .stream()
                .filter(card ->
                    card.getCategory().getCategoryName().equals(categoryRequest)
                )
                .filter(Card::getIsEnabled)
                .map(card -> new CardWithoutCategoryWithImageIdDto(
                card.getDescription(),
                card.getTitle(),
                card.getUrl(),
                card.getImageList().isEmpty() ? null : card.getImageList().get(0).getImageId(),
                card.getPublication(),
                new LocationDTO(
                        card.getLocation().getCountry(),
                        card.getLocation().getCity()
                )
                ))
                .collect(Collectors.toList());
    }

    public List<CardDTO> findCardsByCategoryAndLocation(
            String categoryRequest,
            String country,
            String city
    ) {
        return cardService
                .getAllCards()
                .stream()
                .filter(card ->
                        card.getCategory().getCategoryName().equals(categoryRequest) &&
                        card.getLocation().getCountry().equals(country) &&
                        card.getLocation().getCity().equals(city)
                )
                .filter(Card::getIsEnabled)
                .map(card -> new CardDTO(
                        card.getDescription(),
                        card.getTitle(),
                        card.getUrl(),
                        card.getCategory().getCategoryName(),
                        card.getPublication(),
                        card.getIsEnabled(),
                        new LocationDTO(
                                card.getLocation().getCountry(),
                                card.getLocation().getCity()
                        ),
                        card.getImageList().get(0).getImageId()
                )).collect(Collectors.toList());
    }

    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
