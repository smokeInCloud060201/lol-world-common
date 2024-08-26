package vn.com.lol.common.exceptions;

/**
 * The ResourceNotFoundException exception use when can not find to record
 *
 * @author Ngoc Khanh
 */
public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
