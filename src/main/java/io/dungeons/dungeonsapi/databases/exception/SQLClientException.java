package io.dungeons.dungeonsapi.databases.exception;

public class SQLClientException extends Exception {
    public SQLClientException(String message) {
        super(message);
    }

    public SQLClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
