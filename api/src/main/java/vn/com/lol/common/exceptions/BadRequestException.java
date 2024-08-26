package vn.com.lol.common.exceptions;


/**
 * The BadRequestException exception use when DTO request can not pass the validator
 *
 * @author Ngoc Khanh
 */
public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
