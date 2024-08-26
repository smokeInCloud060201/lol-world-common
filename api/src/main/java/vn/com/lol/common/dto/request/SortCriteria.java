package vn.com.lol.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortCriteria {
    private String field;
    @Builder.Default
    private boolean isASC = true;
    @Builder.Default
    private boolean isIgnoreCase = false;
}