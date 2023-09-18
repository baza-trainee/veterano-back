package com.zdoryk.data.dto;

import java.io.Serializable;
import java.util.List;

public record PartnersPagination(
    List<PartnerDTO> partnerDTOList,
    Integer totalPages,
    Integer totalSize

) implements Serializable {
}
