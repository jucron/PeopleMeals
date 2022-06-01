package com.example.peoplemeals.helpers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONArray;

import java.time.LocalTime;
import java.util.List;

public class JsonConverter {

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static JSONArray localTimeAsJsonArray(final LocalTime localTime) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(List.of(localTime.getHour(),localTime.getMinute()));
        return jsonArray;
    }
}
