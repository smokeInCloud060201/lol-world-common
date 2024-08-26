package vn.com.lol.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.com.lol.common.dto.request.FilterCriteria;
import vn.com.lol.common.dto.request.SearchDTO;
import vn.com.lol.common.dto.request.SortCriteria;
import vn.com.lol.common.enums.ObjectType;
import vn.com.lol.common.enums.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchMapper {

    public static SearchDTO mappingFromRequestParamsToSearchDTO(int pageIndex, int pageSize, String searchKey, String sorts, String filters) {

        return SearchDTO.builder()
                .page(pageIndex)
                .size(pageSize)
                .searchAllKey(Optional.of(searchKey).orElse(null))
                .sortList(mapStringToSortCriteriaList(sorts))
                .filterList(mapStringToFilterCriteriaList(filters))
                .build();
    }

    private static List<SortCriteria> mapStringToSortCriteriaList(String sorts) {
        if (StringUtils.isEmpty(sorts)) {
            return new ArrayList<>();
        }
        String[] sortCriteriaArr = sorts.split(";");
        List<SortCriteria> sortCriteriaList = new ArrayList<>();

        for (String sortCriterion : sortCriteriaArr) {
            String[] sortCriteria = sortCriterion.split(",");
            int length = sortCriteria.length;
            sortCriteriaList.add(SortCriteria.builder()
                            .field(sortCriteria[0])
                            .isASC(length < 2 || Boolean.parseBoolean(sortCriteria[1]))
                            .isIgnoreCase(length >= 3 && Boolean.parseBoolean(sortCriteria[2]))
                    .build());
        }

        return sortCriteriaList;
    }

    private static List<FilterCriteria> mapStringToFilterCriteriaList(String filters) {
        if (StringUtils.isEmpty(filters)) {
            return new ArrayList<>();
        }
        String[] filtersArr = filters.split(";");
        int length = filtersArr.length;
        List<FilterCriteria> filterCriteriaList = new ArrayList<>();
        for (String filterCriterion : filtersArr) {
            String[] filterCriterionArr = filterCriterion.split(";");
            filterCriteriaList.add(FilterCriteria.builder()
                            .field(filterCriterionArr[0])
                            .value(filtersArr[1])
                            .operator(length >= 3 ? Operator.valueOf(filtersArr[2]) : Operator.EQUAL)
                            .type(length >= 4 ? ObjectType.valueOf(filtersArr[3]) : ObjectType.STRING)
                            .andFlag(length < 5 || Boolean.parseBoolean(filtersArr[4]))
                    .build());
        }

        return filterCriteriaList;
    }
}
