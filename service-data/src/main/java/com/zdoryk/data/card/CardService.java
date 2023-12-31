package com.zdoryk.data.card;

import com.zdoryk.data.category.Category;
import com.zdoryk.data.category.CategoryService;
import com.zdoryk.data.dto.*;
import com.zdoryk.data.exception.NotFoundException;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.image.ImageService;
import com.zdoryk.data.location.Location;
import com.zdoryk.data.location.LocationService;
import com.zdoryk.data.mappers.CardMapper;
import com.zdoryk.data.subscription.SubscriptionService;
import com.zdoryk.data.url.Url;
import com.zdoryk.data.url.UrlService;
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
import java.util.*;
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
    private final UrlService urlService;
    private final CardMapper cardMapper;

    @Transactional
    @CacheEvict(cacheNames = "cards", allEntries = true)
    public void saveCard(CardSaveRequest cardSaveRequest) {

        String country = cardSaveRequest.getLocation().getCountry();
        String city = cardSaveRequest.getLocation().getCity();

        Location location = locationService
                .getLocationByCountryAndCity(new LocationDTO(city,country))
                .orElseGet(() -> new Location(country, city));

        cardSaveRequest.getCategories().forEach(
                x -> {
                    String modifiedName = x.getCategoryName() + " ";
                    modifiedName = modifiedName.replaceAll("\\s+", " ");
                    x.setCategoryName(modifiedName);
                }
        );
        cardSaveRequest.setCategories(new ArrayList<>(
                new HashSet<>(cardSaveRequest.getCategories()))
        );


        Url url = Url.builder()
                        .url(cardSaveRequest.getUrl().replaceFirst("^https?://", ""))
                        .visitors(0L)
                        .build();

        Card card = Card.builder()
                .title(cardSaveRequest.getTitle())
                .description(cardSaveRequest.getDescription())
                .url(url)
                .publication(cardSaveRequest.getPublication())
                .isEnabled(false)
                .isActive(false)
                .build();

        List<Category> categories = cardSaveRequest.getCategories().stream()
                .map(category -> categoryService.findCategoryByName(category.getCategoryName())
                        .orElseGet(() -> new Category(category.getCategoryName()))
                )
                .collect(Collectors.toList());

        categories.forEach(cur -> {
            if(cur.getCardList() == null){
                cur.setCardList(new ArrayList<>());
            }
                cur.getCardList().add(card);
        });

        url.setCard(card);

        Image image = Image.builder()
                .image(cardSaveRequest.getImage())
                .card(card)
                .build();

        if (location.getCardList() == null) {
            location.setCardList(new ArrayList<>());
        }

        location.getCardList().add(card);
        card.setLocation(location);
        card.setImage(image);
        card.setCategories(categories);
        categoryService.saveCategories(categories);
        locationService.saveLocation(location);
        cardRepository.save(card);
        urlService.saveUrl(url);
        imageService.saveImage(image);
    }


    @Transactional
    @CacheEvict(cacheNames = "cards", allEntries = true)
    public void updateCard(UpdateCardDTO cardDTO){

        Card card = cardRepository.findById(cardDTO.getCardId())
                .orElseThrow(() -> new NotFoundException("NOT FOUND"));

        if(cardDTO.getIsEnabled() != null){
            card.setIsActive(cardDTO.getIsEnabled());
        }
        if(cardDTO.getDescription() != null && !cardDTO.getDescription().isEmpty()){
            card.setDescription(cardDTO.getDescription());
        }
        if(cardDTO.getTitle() != null && !cardDTO.getTitle().isEmpty()){
            card.setTitle(cardDTO.getTitle());
        }
        if(cardDTO.getPublication() != null){
            card.setPublication(cardDTO.getPublication());
            if(card.getPublication().isAfter(LocalDate.now())){
                card.setIsEnabled(false);
                card.setIsActive(false);
            }
        }
        if(cardDTO.getUrl() != null && !cardDTO.getUrl().isEmpty()) {
            Url url = card.getUrl();
            url.setUrl(cardDTO.getUrl());
            urlService.saveUrl(url);
        }
        if(cardDTO.getImage() != null && !cardDTO.getImage().isEmpty()){
           imageService.deleteImage(card.getImage());
           Image image = Image
                   .builder()
                   .image(cardDTO.getImage())
                   .card(card)
                   .build();
           card.setImage(image);
           imageService.saveImage(image);
        }
        if(cardDTO.getLocation() != null){
            String country = cardDTO.getLocation().getCountry();
            String city = cardDTO.getLocation().getCity();
            Location location = locationService
                    .getLocationByCountryAndCity(new LocationDTO(city,country))
                    .orElseGet(() -> new Location(country, city));

            if (location.getCardList() == null) {
                location.setCardList(new ArrayList<>());
            }
            location.getCardList().add(card);
            locationService.saveLocation(location);
            card.setLocation(location);
        }
        if(cardDTO.getCategories() != null && !cardDTO.getCategories().isEmpty()){

            List<Category> categories = cardDTO.getCategories().stream()
                    .map(category -> categoryService.findCategoryByName(category.getCategoryName())
                            .orElseGet(() -> new Category(category.getCategoryName()))
                    )
                    .collect(Collectors.toList());

            categories.forEach(cur -> {
                if(cur.getCardList() == null){
                    cur.setCardList(new ArrayList<>());
                }
                cur.getCardList().add(card);
            });

            card.setCategories(categories);
            categoryService.saveCategories(categories);
        }
        cardRepository.save(card);
    }


    @Cacheable
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public CardsPagination getAllCardsForAdmin(Integer pageNumber, Integer pageSize){
        List<Card> cards = getAllCards();
        int totalItems = cards.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<CardDTO> cardDTOS = cards
                .subList(startIndex, endIndex)
                .stream()
                .map(cardMapper::toCardDTO)
                .collect(Collectors.toList());

        Collections.reverse(cardDTOS);
        return new CardsPagination(
                cardDTOS,
                totalPages,
                totalItems
        );
    }


    @Transactional
//    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    @Scheduled(fixedRate = 10000L)
    public void processNewCardsAndNotifySubscribers(){
        List<Card> cards = cardRepository
                .findCardByIsEnabledFalse()
                .stream()
                .filter(card ->
                        card.getPublication().isEqual(LocalDate.now()) ||
                        card.getPublication().isBefore(LocalDate.now()))
                .toList();

        if(!cards.isEmpty()){
            cards.forEach(card -> {card.setIsEnabled(true);card.setIsActive(true);});
            cardRepository.saveAll(cards);
            subscriptionService.notifyAllSubscriberAboutNewCards(cards);
        }

    }

    public List<Card> getAllActivatedCards() {
        return cardRepository.findCardByIsActiveTrue();
    }

    public CardDTO getCardById(Long id) {
        Card card =  cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NOT FOUND"));
        return cardMapper.toCardDTO(card);
    }

    @CacheEvict(cacheNames = "cards", allEntries = true)
    public void deleteCardById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NOT FOUND"));

        Location location = card.getLocation();
        location.getCardList().remove(card);
        locationService.saveLocation(location);
        cardRepository.delete(card);
    }
}
