package nl.dignitas.evently.dto;

import java.util.Map;

public record SubmissionResponse(
        Long id,
        Long formId,
        Map<String, Object> answers
) {
}
