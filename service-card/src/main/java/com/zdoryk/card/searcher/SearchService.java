package com.zdoryk.card.searcher;

import com.zdoryk.card.card.Card;
import com.zdoryk.card.card.CardService;
import com.zdoryk.card.category.Category;
import com.zdoryk.card.category.CategoryRepository;
import com.zdoryk.card.category.CategoryService;
import com.zdoryk.card.dto.*;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final CardService cardService;
    private final ImageService imageService;
    private final LocationService locationService;
    private final CategoryService categoryService;


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
            throw new NotFoundException("By this location no available cards");
        }

        return  cards
                .stream()
                .map(card -> new CardWithOutLocation(
                card.getDescription(),
                card.getTitle(),
                card.getUrl(),
                card.getCategory().getCategoryName(),
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
                .map(card -> new CardWithoutCategoryWithImageIdDto(
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
                .map(card -> new CardDTO(
                        card.getDescription(),
                        card.getTitle(),
                        card.getUrl(),
                        card.getCategory().getCategoryName(),
                        new LocationDTO(
                                card.getLocation().getCountry(),
                                card.getLocation().getCity()
                        )
                )).collect(Collectors.toList());
    }

    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
