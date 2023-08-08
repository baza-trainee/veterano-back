package com.zdoryk.card.card;

public enum Category {

    TEST("TEST");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

