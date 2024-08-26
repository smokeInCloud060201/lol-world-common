package vn.com.lol.common.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import vn.com.lol.common.dto.response.BaseResponse;
import vn.com.lol.common.exceptions.BadRequestException;
import vn.com.lol.common.exceptions.ResourceNotFoundException;
import vn.com.lol.common.enums.ExceptionErrorCode;

import java.util.List;
import java.util.Optional;

/**
 * The BaseController class use to handle global exception after exception throws
 *
 * @author Ngoc Khanh
 */
@RestController
public class BaseController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<BaseResponse<Object>> handleAbstractException(Exception e) {
        return buildErrorResponse(e, ExceptionErrorCode.E_001, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse<Object>> handleNotFoundException(Exception e) {
        return buildErrorResponse(e, ExceptionErrorCode.E_002, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class, HandlerMethodValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse<Object>> handleBadRequest(Exception e) {
        return buildErrorResponse(e, ExceptionErrorCode.E_003, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        List<String> validationErrors = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
    return buildErrorResponse(e, ExceptionErrorCode.E_003, HttpStatus.BAD_REQUEST, validationErrors);
    }

    /**
     * The buildErrorResponse method use to build base response DTO
     *
     * @param e type of exception.
     * @param exceptionErrorCode type of errorCode enum.
     * @return BaseResponseDTO
     *
     * @author Ngoc Khanh
     */
    protected ResponseEntity<BaseResponse<Object>> buildErrorResponse(Exception e, ExceptionErrorCode exceptionErrorCode, HttpStatus status, List<String> validationErrors) {
        String errorMessage;
        if (validationErrors == null || validationErrors.isEmpty()) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = String.format("Validation failed: %s", String.join(", ", validationErrors));
        }
        return ResponseEntity.status(status)
                .body(BaseResponse.builder()
                .errorMessage(errorMessage)
                .errorCode(Optional.of(exceptionErrorCode)
                        .orElse(ExceptionErrorCode.E_001))
                .build());
    }

    protected ResponseEntity<BaseResponse<Object>> buildErrorResponse(Exception e, ExceptionErrorCode exceptionErrorCode, HttpStatus status) {
        return buildErrorResponse(e, exceptionErrorCode, status, null);
    }
}
