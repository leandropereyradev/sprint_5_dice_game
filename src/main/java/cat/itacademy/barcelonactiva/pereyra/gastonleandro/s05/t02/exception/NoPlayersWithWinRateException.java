package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoPlayersWithWinRateException extends RuntimeException {
    public NoPlayersWithWinRateException(String message) {
        super(message);
    }
}
