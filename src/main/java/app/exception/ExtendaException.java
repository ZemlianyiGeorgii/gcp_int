package app.exception;

public class ExtendaException extends Exception {
    public ExtendaException() {
        super();
    }

    public ExtendaException(String message) {
        super(message);
    }

    public ExtendaException(String message, Throwable cause) {
        super(message, cause);
    }
}
