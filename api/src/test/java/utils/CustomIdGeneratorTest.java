package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import vn.com.lol.common.utils.CustomIdGenerator;

import static org.mockito.ArgumentMatchers.any;

class CustomIdGeneratorTest {

    @Mock
    private CustomIdGenerator customIdGenerator;

    @BeforeEach
    void setup() {
        customIdGenerator = new CustomIdGenerator();
    }

    @Test
    void shouldReturnGenerateValueIsNotNull() {
        Object generatedValue = customIdGenerator.generate(any(), any());
        Assertions.assertNotNull(generatedValue);
    }
}
