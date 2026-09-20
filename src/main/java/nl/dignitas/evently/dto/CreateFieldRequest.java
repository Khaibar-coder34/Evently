package nl.dignitas.evently.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.dignitas.evently.domain.FieldType;

import java.util.List;

// Een veld dat binnenkomt bij bijvoorbeeld POST /api/forms
public record CreateFieldRequest(

    @NotBlank(message = "Veldnaam is verplicht")
    String name,

    @NotBlank(message = "veldlabel is verplicht")
    String label,

    @NotNull(message = "veldtype is verplicht")
    FieldType type,

    boolean required,

    List<@NotBlank(message = "Choice optie kan niet leeg zijn") String > options
    ) {
}
