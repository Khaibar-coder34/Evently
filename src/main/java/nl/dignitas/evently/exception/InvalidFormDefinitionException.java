package nl.dignitas.evently.exception;

// Wordt gebruikt wanneer iemand probeert een formulier te maken dat zelf niet gedlig is
public class InvalidFormDefinitionException extends RuntimeException {
    public InvalidFormDefinitionException(String message) {
        super(message);
    }
}
