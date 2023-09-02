package com.zdoryk.data.mappers;

import com.zdoryk.data.dto.PartnerDTO;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.info.partner.Partner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    PartnerDTO toPartnerDTO(Partner partner);

    default String map(Image image) {
        if (image == null) {
            return null;
        }
        return image.getImageId().toString();
    }
    
}
