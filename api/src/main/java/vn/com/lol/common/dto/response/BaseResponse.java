package vn.com.lol.common.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import vn.com.lol.common.enums.ExceptionErrorCode;

import java.util.Map;

@Builder
@Getter
public class BaseResponse <T> {
    @JsonProperty("data")
    private T data;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("error_code")
    private ExceptionErrorCode errorCode;

    @JsonProperty("meta_data")
    private Map<String, Object> metaData;
}
