package dev.soulcatcher.exceptions;

public class TokenParseException extends UnauthorizedException {
    public TokenParseException() {
    }

    public TokenParseException(String message) {
        super(message);
    }

    public TokenParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenParseException(Throwable cause) {
        super(cause);
    }

    public TokenParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
