package nl.dignitas.evently.exception;

import java.util.Map;

public class InvalidSubmissionException extends RuntimeException{
    private final Map<String, String> errors;

    public InvalidSubmissionException(Map<String, String> errors) {
        super("Validatie foutmelding");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
