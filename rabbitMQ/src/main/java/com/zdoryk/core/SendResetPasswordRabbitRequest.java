package com.zdoryk.core;

import java.io.Serializable;

public record SendResetPasswordRabbitRequest(

        String email,
        String firstName,
        Integer status,
        Long order) implements Serializable
{}
