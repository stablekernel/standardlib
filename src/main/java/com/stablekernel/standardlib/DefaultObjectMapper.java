package com.stablekernel.standardlib;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class DefaultObjectMapper extends ObjectMapper {

    public DefaultObjectMapper() {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, BuildConfig.DEBUG);
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.DEFAULT);
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
