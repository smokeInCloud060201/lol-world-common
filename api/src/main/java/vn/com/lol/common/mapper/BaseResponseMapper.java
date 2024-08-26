package vn.com.lol.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import vn.com.lol.common.dto.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponseMapper {

    public static <T> BaseResponse<T> of(T data) {
        return BaseResponse.<T>builder()
                .data(data)
                .build();
    }

    public static <T> BaseResponse<List<T>> ofPageable(Page<T> data) {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("total_items", data.getTotalElements());
        metaData.put("size", data.getSize());
        metaData.put("page", data.getTotalPages());
        return BaseResponse.<List<T>>builder()
                .data(data.getContent())
                .metaData(metaData)
                .build();
    }
}
