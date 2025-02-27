package ru.slava.manager_app.exeption;

import java.util.Arrays;
import java.util.List;

public class BadRequestExeption extends RuntimeException {
    private final List<String> errors;

    public BadRequestExeption(List<String> errors) {
        this.errors = errors;
    }

    public BadRequestExeption(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public BadRequestExeption(Throwable cause, List<String> errors) {
        super(cause);
        this.errors = errors;
    }

    public BadRequestExeption(String message, Throwable cause, List<String> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public BadRequestExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
            List<String> errors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errors = errors;
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
        BadRequestExeption other = (BadRequestExeption) obj;
        if (errors == null) {
            if (other.errors != null)
                return false;
        } else if (!errors.equals(other.errors))
            return false;
        return true;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "BadRequestExeption [errors=" + errors + ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
                + ", getErrors()=" + getErrors() + ", getMessage()=" + getMessage() + ", getLocalizedMessage()="
                + getLocalizedMessage() + ", getCause()=" + getCause() + ", toString()=" + super.toString()
                + ", fillInStackTrace()=" + fillInStackTrace() + ", getStackTrace()=" + Arrays.toString(getStackTrace())
                + ", getSuppressed()=" + Arrays.toString(getSuppressed()) + "]";
    }
}
