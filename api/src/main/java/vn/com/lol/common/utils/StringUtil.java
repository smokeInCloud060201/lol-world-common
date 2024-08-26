package vn.com.lol.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

    public static String convertSnakeToCamel(String snakeCase) {
        return Pattern.compile("_([a-z])")
                .matcher(snakeCase)
                .replaceAll(m -> m.group(1).toUpperCase());
    }

}
