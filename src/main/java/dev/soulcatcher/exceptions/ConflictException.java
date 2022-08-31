package dev.soulcatcher.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException() {
    }

    public ConflictException(String s) {
        super(s);
    }

    public ConflictException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConflictException(Throwable throwable) {
        super(throwable);
    }

    public ConflictException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
