package com.example.admin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Constants {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final String BASE_URL = "https://qpkkklllk.execute-api.ap-south-1.amazonaws.com";

    public static final String PLACED = "PLACED";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PICKED = "PICKED";
}
