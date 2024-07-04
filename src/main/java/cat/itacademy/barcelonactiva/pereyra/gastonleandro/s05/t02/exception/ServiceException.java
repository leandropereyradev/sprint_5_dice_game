package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
