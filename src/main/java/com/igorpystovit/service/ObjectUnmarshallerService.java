package com.igorpystovit.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ObjectUnmarshallerService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param file  file to be deserialized
     * @param clazz target deserialization result class
     * @param <T>   type of target deserialization class
     * @return instance of class {@code T} deserialized from {@link File}
     */
    public <T> T fromJson(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize incoming file", e);
        }
    }

    /**
     * @param file  file to serialize to
     * @param object target to be serialized
     * @param <T>   type of target serialization class
     */
    public <T> void toJson(File file, T object) {
        try {
            objectMapper.writeValue(file, object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize data", e);
        }
    }
}
