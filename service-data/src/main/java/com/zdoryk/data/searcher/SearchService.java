package com.zdoryk.data.searcher;

import com.zdoryk.data.card.Card;
import com.zdoryk.data.card.CardService;
import com.zdoryk.data.category.CategoryService;
import com.zdoryk.data.dto.*;
import com.zdoryk.data.exception.NotValidFieldException;
import com.zdoryk.data.info.InfoService;
import com.zdoryk.data.location.LocationService;
import com.zdoryk.data.mappers.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final CardService cardService;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final InfoService infoService;
    private final CardMapper cardMapper;


    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories()
                .stream()
                .filter(category -> category.getCardList().stream().anyMatch(Card::getIsActive))
                .map(category -> new CategoryDto(
                        category.getCategoryName())
                )
                .collect(Collectors.toList());
    }


    public List<CategoryDto> getAllCategoriesAdmin() {
        return categoryService.getAllCategories()
                .stream()
                .map(category -> new CategoryDto(
                        category.getCategoryName())
                )
                .collect(Collectors.toList());
    }


    public CardsPagination findCardByAllQueries(String queue,
                                                LinkedList<String> countries,
                                                LinkedList<String> cities,
                                                String category,
                                                Integer pageNumber,
                                                Integer pageSize
    ) {
        if(!queue.isEmpty()) {
            if (queue
                    .replaceAll("[a-zA-ZА-Яа-яЁё]", "")
                    .matches("[0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?\\\\|]+") ||
                    queue.length() > 50) {
                throw new NotValidFieldException("Queue not matches to pattern");
            }
        }

        List<LocationDTO> locationList = new ArrayList<>();
        if (countries != null && cities != null){
            if(countries.size() == cities.size()){
                for (int i = 0; i < countries.size(); i++) {
                    LocationDTO locationDto = new LocationDTO(
                            cities.get(i).toLowerCase(),
                            countries.get(i).toLowerCase()
                    );
                    locationList.add(locationDto);
                }
            }
        }
        List<Card> cards = cardService.getAllActivatedCards();
        if(!queue.isEmpty()) {
            cards = cards
                    .stream()
                    .filter(card -> card.getTitle().toLowerCase().contains(queue.toLowerCase()) ||
                            card.getDescription().toLowerCase().contains(queue.toLowerCase())
                    )
                    .collect(Collectors.toList());
        }
        if (!locationList.isEmpty()){
            cards = cards.stream()
                    .filter(card -> locationList.stream()
                            .anyMatch(locationDTO ->
                                            locationDTO.getCity().toLowerCase().startsWith(card.getLocation().getCity().toLowerCase()) &&
                                            locationDTO.getCountry().toLowerCase().startsWith(card.getLocation().getCountry().toLowerCase())

                            )
                    )
                    .collect(Collectors.toList());

        }
        if (category != null && !category.isEmpty()) {
            String finalCategory = category.toLowerCase().replaceAll(" ", "");

            cards = cards
                        .stream()
                        .filter(card ->
                                card.getCategories()
                                        .stream()
                                        .anyMatch(cat -> cat
                                                .getCategoryName()
                                                .toLowerCase()
                                                .replaceAll(" ","")
                                                .startsWith(finalCategory)
                                        )

                        )
                        .collect(Collectors.toList());

        }

        int totalItems = cards.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        Collections.reverse(cards);
        List<CardDTO> cardWithImageIds = new ArrayList<>(cards
                .subList(startIndex, endIndex)
                .stream()
                .filter(Card::getIsActive)
                .map(cardMapper::toCardDTO)
                .toList());


        return new CardsPagination(
                cardWithImageIds,
                totalPages,
                totalItems
        );
    }

    public List<LocationDTO> getAllLocations() {
        return locationService.getAll()
                .stream()
                .filter(location -> location.getCardList().stream().anyMatch(Card::getIsActive))
                .map(location -> new LocationDTO(
                        location.getCity(),
                        location.getCountry()
                ))
                .collect(Collectors.toList());
    }

    public CardDTO findCardById(Long id) {
        return cardService.getCardById(id);
    }

    public PartnersPagination getAllPartners(int page, int size) {
        PartnersPagination partners = infoService.getAllPartners(page,size);
        List<PartnerDTO> partnerDTOList = partners.partnerDTOList()
                .stream()
                .filter(PartnerDTO::isEnabled)
                .toList();

        if(!partnerDTOList.isEmpty()) {
            return new PartnersPagination(
                    partnerDTOList,
                    partners.totalPages(),
                    partners.totalSize()
            );
        }
        return new PartnersPagination(
                partnerDTOList,
                0,
                0
        );
    }
}
