package com.zdoryk.data.dto;


import java.io.Serializable;
import java.util.List;

public record CardsPagination (
        List<CardDTO> cards,
        Integer totalPages,
        Integer totalSize
)  implements Serializable { }
