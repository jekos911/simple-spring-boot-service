package ru.jb.db_spring.api;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(assignableTypes = {PaymentController.class})
public class PaymentExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ProblemDetail onClientError(HttpClientErrorException ex) {
        var body = ex.getResponseBodyAs(ProblemDetail.class);
        if (body != null) {
            return body;
        }
        var pd = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage());
        pd.setTitle("Upstream client error");
        return pd;
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ProblemDetail onNetwork(ResourceAccessException e) {
        var pd = ProblemDetail.forStatusAndDetail(GATEWAY_TIMEOUT, "Upstream timeout or network error");
        pd.setTitle("Upstream payments error");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail onValidation(MethodArgumentNotValidException ex) {
        var pd = ProblemDetail.forStatus(BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail onAny(Exception ex) {
        var pd = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, ex.getMessage());
        pd.setTitle("Payments internal error");
        return pd;
    }
}
