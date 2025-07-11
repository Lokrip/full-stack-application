package ru.slava.customer_app.client.exception;

import java.util.Arrays;
import java.util.List;

public class ClientBadRequestException extends RuntimeException {
    private final List<String> errors;

    public ClientBadRequestException(Throwable cause, List<String> errors) {
        super(cause);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errors == null) ? 0 : errors.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClientBadRequestException other = (ClientBadRequestException) obj;
        if (errors == null) {
            if (other.errors != null)
                return false;
        } else if (!errors.equals(other.errors))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ClientBadRequestException [errors=" + errors + ", getErrors()=" + getErrors() + ", hashCode()="
                + hashCode() + ", getClass()=" + getClass() + ", getMessage()=" + getMessage()
                + ", getLocalizedMessage()=" + getLocalizedMessage() + ", getCause()=" + getCause() + ", toString()="
                + super.toString() + ", fillInStackTrace()=" + fillInStackTrace() + ", getStackTrace()="
                + Arrays.toString(getStackTrace()) + ", getSuppressed()=" + Arrays.toString(getSuppressed()) + "]";
    }
}
