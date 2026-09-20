package nl.dignitas.evently.controller;

import jakarta.validation.Valid;
import nl.dignitas.evently.dto.CreateFormRequest;
import nl.dignitas.evently.dto.FormResponse;
import nl.dignitas.evently.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormResponse createForm(@Valid @RequestBody CreateFormRequest request) {
        return formService.createForm(request);
    }
}
