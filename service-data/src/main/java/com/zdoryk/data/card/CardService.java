package com.zdoryk.data.card;

import com.zdoryk.data.category.Category;
import com.zdoryk.data.category.CategoryService;
import com.zdoryk.data.dto.CardSaveRequest;
import com.zdoryk.data.dto.CardWithImageIdDto;
import com.zdoryk.data.dto.LocationDTO;
import com.zdoryk.data.dto.LocationFindRequest;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.image.ImageService;
import com.zdoryk.data.location.Location;
import com.zdoryk.data.location.LocationService;
import com.zdoryk.data.subscription.Subscription;
import com.zdoryk.data.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cards")
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final LocationService locationService;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;

    @CacheEvict(cacheNames = "cards", allEntries = true)
    @Transactional
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
                .publication(cardSaveRequest.getPublication())
                .category(category)
                .isEnabled(false)
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

    public List<CardWithImageIdDto> getAllCardsWithImageId(int page,int size) {
        return cardRepository
                .findAll(PageRequest.of(page,size))
                .getContent()
                .stream()
                .map(card -> new CardWithImageIdDto(
                        card.getDescription(),
                        card.getTitle(),
                        card.getUrl(),
                        card.getImageList().isEmpty() ? null : card.getImageList().get(0).getImageId(),
                        card.getPublication(),
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


    @Transactional
//    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    @Scheduled(fixedRate = 10000L)
    public void processNewCardsAndNotifySubscribers(){
        log.info("looking and enabling cards");
        List<Card> cards = cardRepository
                .findCardByIsEnabledFalse()
                .stream()
                .filter(card ->
                        card.getPublication().isEqual(LocalDate.now()) ||
                        card.getPublication().isBefore(LocalDate.now()))
                .toList();

        if(!cards.isEmpty()){
            cards.forEach(card -> card.setIsEnabled(true));
            cardRepository.saveAll(cards);
            subscriptionService.notifyAllSubscriberAboutNewCards(cards);
        }

    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        char firstChar = Character.toUpperCase(input.charAt(0));
        return firstChar + input.substring(1);
    }
}
