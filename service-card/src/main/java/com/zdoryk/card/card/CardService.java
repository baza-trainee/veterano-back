package com.zdoryk.card.card;

import com.zdoryk.card.category.Category;
import com.zdoryk.card.category.CategoryService;
import com.zdoryk.card.dto.CardSaveRequest;
import com.zdoryk.card.dto.CardWithImageIdDto;
import com.zdoryk.card.dto.LocationDTO;
import com.zdoryk.card.dto.LocationFindRequest;
import com.zdoryk.card.image.Image;
import com.zdoryk.card.image.ImageService;
import com.zdoryk.card.location.Location;
import com.zdoryk.card.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@CacheConfig(cacheNames = "cards")
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final LocationService locationService;
    private final ImageService imageService;
    private final CategoryService categoryService;

    @Transactional
    @CacheEvict(cacheNames = "cards", allEntries = true)
    public void saveCard(CardSaveRequest cardSaveRequest) {

        String country = capitalizeFirstLetter(cardSaveRequest.getLocation().getCountry());
        String city = capitalizeFirstLetter(cardSaveRequest.getLocation().getCity());

        Location location = locationService
                .getLocationByCountryAndCity(new LocationFindRequest(city,country))
                .orElseGet(() -> new Location(country, city));

        Category category = categoryService
                .findCategoryByName(cardSaveRequest.getCategory())
                .orElseGet(Category::new);

        category.setCategoryName(cardSaveRequest.getCategory());
        Card card = Card.builder()
                .title(cardSaveRequest.getTitle())
                .description(cardSaveRequest.getDescription())
                .url(cardSaveRequest.getUrl())
                .category(category)
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
        card.setCategory(category);
        categoryService.saveCategory(category);
        locationService.saveLocation(location);
        cardRepository.save(card);
        imageService.saveImage(image);
    }

    @Cacheable
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public List<CardWithImageIdDto> getAllCardsWithImageId() {
        return cardRepository
                .findAll()
                .stream()
                .map(card -> new CardWithImageIdDto(
                        card.getDescription(),
                        card.getTitle(),
                        card.getUrl(),
                        card.getImageList().isEmpty() ? null : card.getImageList().get(0).getImageId(),
                        new LocationDTO(
                                card.getLocation().getCountry(),
                                card.getLocation().getCity()
                        ),
                        card.getCategory().getCategoryName()
                ))
                .collect(Collectors.toList());
    }

    public List<Card> getCardsByCategoriesAndLocation(
            Category category,
            Location location
    ){
        return cardRepository.findByCategoryAndLocation(category,location);
    }


    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        char firstChar = Character.toUpperCase(input.charAt(0));
        return firstChar + input.substring(1);
    }
}
