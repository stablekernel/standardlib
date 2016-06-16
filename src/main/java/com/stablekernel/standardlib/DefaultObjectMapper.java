package com.stablekernel.standardlib;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DefaultObjectMapper extends ObjectMapper {

    public DefaultObjectMapper(boolean isDebug) {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, isDebug);
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.DEFAULT);
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        setTimeZone(TimeZone.getTimeZone("UTC"));
    }
}
