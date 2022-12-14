package server.footballmanager.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NotEnoughMoneyAdvice {
    @ResponseBody
    @ExceptionHandler(NotEnoughMoneyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notEnoughMoneyHandler(NotEnoughMoneyException ex) {
        return ex.getMessage();
    }
}
