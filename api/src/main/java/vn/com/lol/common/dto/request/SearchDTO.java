package vn.com.lol.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import vn.com.lol.common.constants.PagingConstant;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchDTO {
    @Builder.Default
    private int page = PagingConstant.DEFAULT_PAGE_INDEX;
    @Builder.Default
    private int size = PagingConstant.DEFAULT_PAGE_SIZE;
    private String searchAllKey;
    @Singular(value = "sortList")
    private List<SortCriteria> sortList;
    @Singular(value = "filterList")
    private List<FilterCriteria> filterList = new ArrayList<>();
}