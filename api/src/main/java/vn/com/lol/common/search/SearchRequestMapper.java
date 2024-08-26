package vn.com.lol.common.search;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import vn.com.lol.common.annotations.SearchAllForKey;
import vn.com.lol.common.constants.PagingConstant;
import vn.com.lol.common.dto.request.SortCriteria;
import vn.com.lol.common.dto.request.FilterCriteria;
import vn.com.lol.common.dto.request.SearchDTO;
import vn.com.lol.common.entities.BaseEntity;
import vn.com.lol.common.enums.Operator;
import vn.com.lol.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchRequestMapper {
    /**
     * Convert Search Request to Pageable
     * @param searchRequest search request
     */
    public static Pageable getPageable(SearchDTO searchRequest) {
        if (searchRequest == null) {
            return PageRequest.of(PagingConstant.DEFAULT_PAGE_INDEX, PagingConstant.DEFAULT_PAGE_SIZE);
        }
        if (searchRequest.getSortList() != null && !searchRequest.getSortList().isEmpty()) {
            Sort sort = Sort.by(searchRequest.getSortList()
                    .stream().map(SearchRequestMapper::convertSortCriteria)
                    .toList());
            return PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);

        }
        return PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

    }

    private static Sort.Order convertSortCriteria(SortCriteria sortCriteria) {
        Sort.Order order = new Sort.Order(sortCriteria.isASC() ? Sort.Direction.ASC : Sort.Direction.DESC,
                StringUtil.convertSnakeToCamel(sortCriteria.getField()));
        return  sortCriteria.isIgnoreCase() ? order.ignoreCase() : order;
    }

    /**
     * Convert Search Request to Specification
     * @param filterList search request
     */
    public static <T> Specification<T> getSpecifications(List<FilterCriteria> filterList) {
        Specification<T> specs = Specification.where(null);
        if (filterList.isEmpty()) {
            return specs;
        }
        filterList.forEach(criteria -> criteria.setField(StringUtil.convertSnakeToCamel(criteria.getField())));
        for (int i = 0; i < filterList.size(); i++) {
            FilterCriteria currentCriteria = filterList.get(i);
            CustomSpecification<T> newSpec = new CustomSpecification<>(currentCriteria);
            if (i == 0) {
                specs = newSpec;
            } else {
                specs = currentCriteria.isAndFlag() ?
                        specs.and(newSpec) :
                        specs.or(newSpec);
            }
        }
        return specs;
    }

    public static <T extends BaseEntity> Specification<T> getSpecForSearchAll(String searchKey, Class<T> clazz) {
        if (searchKey != null && !searchKey.isBlank()) {
            List<FilterCriteria> filterList = getSearchAllFields(clazz)
                    .stream()
                    .map(field -> FilterCriteria.builder()
                            .field(field)
                            .operator(Operator.LIKE)
                            .value(searchKey)
                            .andFlag(false).build())
                    .toList();
            return SearchRequestMapper.getSpecifications(filterList);
        }

        return Specification.where(null);
    }

    private static <T> List<String> getSearchAllFields(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SearchAllForKey.class))
                .flatMap(field -> {
                    String[] childField = field.getAnnotation(SearchAllForKey.class).children();
                    if (childField.length == 0) {
                        return Arrays.stream(new String[]{field.getName()});
                    } else {
                        return Arrays.stream(childField).map(child -> field.getName() + "." + child);
                    }
                })
                .toList();
    }

    /**
     * Add 1=0 condition to sql query to return empty result
     *
     * @return Specification
     * @param <T> type of entity
     */
    public static <T> Specification<T> alwaysFailPredicate() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or();
    }
}
