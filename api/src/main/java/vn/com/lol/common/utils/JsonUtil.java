package vn.com.lol.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import vn.com.lol.common.serializer.SafeSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Slf4j
public class JsonUtil {
    private static final String ERROR_MESSAGE = "EXCEPTION WHEN PARSE OBJECT TO STRING {}";

    private static ObjectMapper mapper;

    private JsonUtil() {}

    private static void initialize() {
        if (mapper != null) {
            return;
        }
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Object.class, new SafeSerializer());

        mapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(NON_NULL)
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }

    public static void setMapper(Module module) {
        initialize();
        mapper.registerModule(module);
    }

    public static JsonNode convertStringToJsonNode(String jsonString) throws IOException {
        initialize();
        return mapper.readTree(jsonString);
    }

    public static String stringify(Object data) {
        initialize();
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error(ERROR_MESSAGE, e.getMessage());
        }
        return null;
    }

    public static void stringify(Object data, OutputStream output) {
        initialize();
        try {
            mapper.writeValue(output, data);
        } catch (IOException e) {
            log.error(ERROR_MESSAGE, e.getMessage());
        }
    }

    /**
     * Gets object from json string. Object MUST have constructor with no parameter
     *
     * @param <T>   the type parameter
     * @param clazz class to convert
     * @param json  the json
     * @return the object from json string
     *
     */
    public static <T> T getObjectFromJsonString(Class<T> clazz, String json) {
        initialize();
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error(ERROR_MESSAGE, e.getMessage());
        }
        return null;
    }

    public static <T> T getObjectFromJsonString(Class<T> clazz, String json, T defaultValue) {
        initialize();
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error(ERROR_MESSAGE, e.getMessage());
        }
        return defaultValue;
    }

    public static <T> T getObjectFromJsonString(TypeReference<T> clazz, String json) {
        initialize();
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error(ERROR_MESSAGE, e.getMessage());
        }
        return null;
    }

    private static <T> T convert(Object obj, TypeReference<T> typeReference) {
        initialize();
        return mapper.convertValue(obj, typeReference);
    }

    public static Map<String, Object> convertToMap(Object obj) {
        return convert(obj, new TypeReference<>() {});
    }

    public static Set<String> convertToSet(Object obj) {
        return convert(obj, new TypeReference<>() {});
    }
}
