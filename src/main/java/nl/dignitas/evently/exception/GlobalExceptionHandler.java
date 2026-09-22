package nl.dignitas.evently.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource niet gevonden",
                Map.of("resource", exception.getMessage())
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(InvalidFormDefinitionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidForm(InvalidFormDefinitionException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Formulier is ongeldig",
                Map.of("form", exception.getMessage())
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(InvalidSubmissionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSubmission(InvalidSubmissionException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validatie mislukt",
                exception.getErrors()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleBeanValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validatie mislukt",
                errors
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableJson(HttpMessageNotReadableException exception) {

        Throwable cause = exception.getMostSpecificCause();

        // Een property bestaat wel, maar de waarde kan niet worden omgezet.
        if (cause instanceof InvalidFormatException invalidFormatException) {

            String fieldName = "request";

            // Zoek welk JSON-field de fout veroorzaakte.
            if (!invalidFormatException.getPath().isEmpty()) {
                var lastPathPart = invalidFormatException
                        .getPath()
                        .getLast();

                if (lastPathPart.getPropertyName() != null) {
                    fieldName = lastPathPart.getPropertyName();
                }
            }

            Object wrongValue = invalidFormatException.getValue();
            Class<?> expectedType = invalidFormatException.getTargetType();

            String errorMessage = "Ongeldige waarde '" + wrongValue + "'";

            // Als het verwachte type een enum is,
            // kunnen we meteen alle toegestane waarden teruggeven.
            if (expectedType != null && expectedType.isEnum()) {

                String allowedValues = Arrays.stream(expectedType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                errorMessage = "Ongeldige waarde '" + wrongValue + "'. Toegestaan: " + allowedValues;
            }

            ApiErrorResponse response = new ApiErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Request bevat een ongeldige waarde",
                    Map.of(fieldName, errorMessage)
            );
            return ResponseEntity.badRequest().body(response);
        }

        // Fallback voor andere kapotte JSON.
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Request body is ongeldig",
                Map.of("request", "Controleer of de JSON correct is opgebouwd")
        );
        return ResponseEntity.badRequest().body(response);
    }
}
