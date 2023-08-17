package com.zdoryk.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUpdateMessage implements Serializable {

    private List<ConsumerDto> consumers;

    private List<CardToSendEmail> cards;

}
