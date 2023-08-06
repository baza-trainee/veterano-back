package com.zdoryk.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserRequest(
        @JsonProperty(value = "firstName") String firstName,
        @JsonProperty(value = "secondName") String secondName,
        @JsonProperty(value = "email") String email,
        @JsonProperty(value = "user_id")UUID id)
{}
