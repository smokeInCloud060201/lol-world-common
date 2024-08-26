package vn.com.lol.common.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.com.lol.common.enums.ObjectType;
import vn.com.lol.common.enums.Operator;

import java.io.Serializable;


@Builder
@Getter
@Setter
public class FilterCriteria implements Serializable {
    private String field;
    @Builder.Default
    private Operator operator = Operator.EQUAL;
    private Object value;
    @Builder.Default
    private ObjectType type = ObjectType.STRING;
    @Builder.Default
    private boolean andFlag = true;
}