package com.zdoryk.data.searcher;

import com.zdoryk.data.card.Card;
import com.zdoryk.data.card.CardService;
import com.zdoryk.data.category.CategoryService;
import com.zdoryk.data.dto.*;
import com.zdoryk.data.exception.NotValidFieldException;
import com.zdoryk.data.info.InfoService;
import com.zdoryk.data.location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final CardService cardService;
    private final LocationService locationService;
    private final CategoryService categoryService;
    private final InfoService infoService;


    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories()
                .stream()
                .map(category -> new CategoryDto(
                        category.getCategoryName()
                ))
                .collect(Collectors.toList());
    }


    public CardsPagination findCardByAllQueries(String queue,
                                                LinkedList<String> countries,
                                                LinkedList<String> cities,
                                                List<String> categories,
                                                Integer pageNumber,
                                                Integer pageSize
    ) {
        if(!queue.isEmpty()) {
            if (queue
                    .replaceAll("[a-zA-Z]", "")
                    .matches("[0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?\\\\|]+") ||
                    queue.length() < 2 || queue.length() > 50) {
                throw new NotValidFieldException("queue not matches to pattern");
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
        System.out.println(locationList);
        if (categories != null && !categories.isEmpty()) {
            categories.replaceAll(s -> s + " ");
            categories.replaceAll(s -> s.replaceAll("\\s+"," "));
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
                                    locationDTO.getCountry().equals(card.getLocation().getCountry()) &&
                                    locationDTO.getCity().equals(card.getLocation().getCity())
                            )
                    )
                    .collect(Collectors.toList());

        }
        if (categories != null && !categories.isEmpty()) {
            cards = cards
                    .stream()
                    .filter(card ->
                            card.getCategories()
                                    .stream()
                                    .anyMatch(category -> categories.contains(category.getCategoryName()))
                    )
                    .toList();
        }

        int totalItems = cards.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<CardDTO> cardWithImageIds = cards
                .subList(startIndex, endIndex)
                .stream()
                .filter(Card::getIsEnabled)
                .map(card -> new CardDTO(
                        card.getCardId(),
                        card.getDescription(),
                        card.getTitle(),
                        card.getUrl().getId(),
                        card.getImageList().isEmpty() ? null : card.getImageList().get(0).getImageId(),
                        card.getPublication(),
                        card.getCategories().stream()
                                .map(x -> new CategoryDto(x.getCategoryName()))
                                .collect(Collectors.toList()),
                        new LocationDTO(
                                card.getLocation().getCountry(),
                                card.getLocation().getCity()
                        ),
                        card.getIsEnabled()
                ))
                .toList();

        return new CardsPagination(
                cardWithImageIds,
                totalPages,
                totalItems
        );
    }

    public List<LocationDTO> getAllCounties() {
        return locationService.getAll()
                .stream()
                .map(location -> new LocationDTO(
                        location.getCountry(),
                        location.getCity()
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

        return new PartnersPagination(
                partnerDTOList,
                partners.totalPages(),
                partners.totalSize()
        );
    }
}
