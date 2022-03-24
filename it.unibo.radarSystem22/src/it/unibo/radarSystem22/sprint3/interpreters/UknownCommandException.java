package it.unibo.radarSystem22.sprint3.interpreters;

public class UknownCommandException extends IllegalArgumentException {
    public UknownCommandException() {
    }

    public UknownCommandException(String s) {
        super(s);
    }

    public UknownCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public UknownCommandException(Throwable cause) {
        super(cause);
    }
}
