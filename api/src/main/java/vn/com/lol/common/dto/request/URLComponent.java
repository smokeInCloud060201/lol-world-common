package vn.com.lol.common.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class URLComponent {
    @Builder.Default
    private String scheme = "https";
    private String host;
    private String path;

    @Singular("queryParam")
    private List<QueryParam> queryParams;
}
