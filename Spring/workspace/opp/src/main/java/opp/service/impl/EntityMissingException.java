package opp.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityMissingException extends RuntimeException {

    public EntityMissingException(Class<?> cls, Object ref) {
        super("Entity with reference " + ref + " of " + cls + " not found.");
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
