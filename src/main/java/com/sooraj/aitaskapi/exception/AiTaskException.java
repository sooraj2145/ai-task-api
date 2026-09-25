package com.sooraj.aitaskapi.exception;

public class AiTaskException extends RuntimeException {
    public AiTaskException(String message) {
        super(message);
    }

    public AiTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
