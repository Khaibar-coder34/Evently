package nl.dignitas.evently.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CreateSubmissionRequest(
        @NotNull(message = "Antwoorden zijn verplicht")
        Map<String, Object> answers
) {
}
