package com.zdoryk.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsumerDto implements Serializable {

    private String email;

    private String name;

}
