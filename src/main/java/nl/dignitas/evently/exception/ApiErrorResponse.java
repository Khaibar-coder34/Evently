package nl.dignitas.evently.exception;

import java.util.Map;

//Onze standaard fout response van de API's
public record ApiErrorResponse(
        int status,
        String message,
        Map<String, String> errors
) {
}
