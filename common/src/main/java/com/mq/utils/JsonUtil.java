package com.mq.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;


/**
 * json转换工具类
 *
 * @author liu
 */
@Slf4j
public class JsonUtil {

    /**
     * jackson对象
     */
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        MAPPER.registerModule(timeModule);
        MAPPER.setTimeZone(TimeZone.getDefault());
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象转换成json字符串
     *
     * @param bean 对象
     * @return json字符串
     */
    public static String beanToJson(Object bean) {
        try {
            return MAPPER.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            log.error("将对象转换成json字符串异常,{},{}", bean, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     */
    public static <T> T jsonToBean(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.warn("将json结果集转化为对象异常,{},{}", jsonData, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型(泛型)
     */
    public static <T> T jsonToBean(String jsonData, TypeReference<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.warn("将json结果集转化为对象异常(泛型),{},{}", jsonData, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error("将json数据转换成pojo对象list异常,{},{}", jsonData, e.getMessage(), e);
        }
        return null;
    }

}

