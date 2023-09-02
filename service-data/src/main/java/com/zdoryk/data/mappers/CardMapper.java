package com.zdoryk.data.mappers;

import com.zdoryk.data.card.Card;
import com.zdoryk.data.dto.CardDTO;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.url.Url;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "imageId",source = "image")
    CardDTO toCardDTO(Card card);


    default UUID map(Url url) {
        return url.getId();
    }

    default Long map(Image image) {
        return image.getImageId();
    }


}
