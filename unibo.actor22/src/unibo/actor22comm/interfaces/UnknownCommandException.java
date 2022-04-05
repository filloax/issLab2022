package unibo.actor22comm.interfaces;

public class UnknownCommandException extends Exception {
    private final static String PREFIX = "Unknown command: ";

    public UnknownCommandException() {
    }

    public UnknownCommandException(String message) {
        super(PREFIX + message);
    }

    public UnknownCommandException(String message, Throwable cause) {
        super(PREFIX + message, cause);
    }

    public UnknownCommandException(Throwable cause) {
        super(cause);
    }
}
