package vn.com.lol.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The HibernateConstant class modifier abstract constants of hibernate or SQL query
 *
 * @author Ngoc Khanh
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalHibernateConstant {
    public static final String IS_NOT_DELETED = "is_deleted = FALSE";
    public static final String IS_DELETED = "is_deleted";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Table {

        public static final String FILE = "file";

        public static final String SET = " SET ";
        public static final String DELETED_BY_ID = " = TRUE WHERE id = ?";
        public static final String UPDATE = " UPDATE ";

        public static final String SOFT_DELETE_BY_ID_QUERY = SET + IS_DELETED + DELETED_BY_ID;

        public static final String SOFT_DELETE_FILE = UPDATE + FILE + SOFT_DELETE_BY_ID_QUERY;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Entity {
        public static final String FILE = "File";
    }
}
