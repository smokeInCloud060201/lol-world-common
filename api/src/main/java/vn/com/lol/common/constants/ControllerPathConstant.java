package vn.com.lol.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The ControllerPathConstant class modifier constants of controller path
 *
 * @author Ngoc Khanh
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerPathConstant {

    public static final String V1 = "/v1";
    public static final String API = "/api";
    public static final String API_V1_PREFIX_BASE_PATH = API + V1;

}
