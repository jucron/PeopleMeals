package com.example.peoplemeals.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestConverter {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
