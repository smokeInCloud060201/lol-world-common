package vn.com.lol.common.exceptions;

public class RecordAlreadyExistsException extends Exception {
    public RecordAlreadyExistsException() {
    }

    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
