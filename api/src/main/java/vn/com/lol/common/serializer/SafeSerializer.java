package vn.com.lol.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SafeSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            ObjectMapper objectMapper = (ObjectMapper) jsonGenerator.getCodec();
            objectMapper.writeValue(jsonGenerator, o);
        } catch (Exception e) {
            jsonGenerator.writeNull(); // Skip problematic fields by writing null
        }
    }
}
