package nl.dignitas.evently.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateFormRequest(
        @NotBlank(message = "formuliernaam is verplicht")
        String name,

        @NotEmpty(message = "Een formulier moet minimal één veld hebben")
        List<@Valid CreateFieldRequest> fields
) {
}
