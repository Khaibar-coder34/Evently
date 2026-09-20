package nl.dignitas.evently.exception;

// Wordt gebruikt wanneer bijvoorbeeld een formulier niet bestaat
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
