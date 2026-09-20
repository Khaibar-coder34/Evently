package nl.dignitas.evently.dto;

import nl.dignitas.evently.domain.FieldType;

import java.lang.reflect.Field;
import java.util.List;

public record FieldResponse(
        Long id,
        String name,
        String label,
        FieldType type,
        boolean required,
        List<String> options
) {
}
