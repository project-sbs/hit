package com.project.hit;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final boolean success;
    public DataNotFoundException(String message) {
        super(message);
        this.success = false;
    }

    public DataNotFoundException(String message, boolean success) {
        super(message);
        this.success = success;
    }
}
