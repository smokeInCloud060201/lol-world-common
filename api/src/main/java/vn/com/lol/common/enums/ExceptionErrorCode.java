package vn.com.lol.common.enums;

/**
 *  The ExceptionErrorCode enum use for return special error code to client
 *
 * @author Ngoc Khanh
 */
public enum ExceptionErrorCode {
    /**
     * INTERNAL_SERVER_ERROR: 5xx
     */
    E_001,

    /**
     * NOT_FOUND: 404
     */
    E_002,

    /**
     * BAD_REQUEST: 400
     */
    E_003,

    /**
     * UN_AUTHORIZE: 401
     */
    E_004,

    /**
     * TOKEN_EXPIRED: 401
     */
    E_005,
}
