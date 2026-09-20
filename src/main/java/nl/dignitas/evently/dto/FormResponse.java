package nl.dignitas.evently.dto;

import java.util.List;

public record FormResponse(
        Long id,
        String name,
        List<FieldResponse> fields
) {
}
