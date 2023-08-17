package com.zdoryk.core;

import java.io.Serializable;

public record SendMessageVerificationRabbitRequest(
        Long orderNumber,
        String firstName,
        String secondName,

        String email,
        String title,
        String author) implements Serializable
{}
