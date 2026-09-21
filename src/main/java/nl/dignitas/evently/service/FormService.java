package nl.dignitas.evently.service;

import nl.dignitas.evently.domain.FieldType;
import nl.dignitas.evently.domain.Form;
import nl.dignitas.evently.domain.FormField;
import nl.dignitas.evently.dto.CreateFieldRequest;
import nl.dignitas.evently.dto.CreateFormRequest;
import nl.dignitas.evently.dto.FieldResponse;
import nl.dignitas.evently.dto.FormResponse;
import nl.dignitas.evently.exception.InvalidFormDefinitionException;
import nl.dignitas.evently.exception.ResourceNotFoundException;
import nl.dignitas.evently.repository.FormRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class FormService {
    private final FormRepository formRepository;

    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @Transactional
    public FormResponse createForm(CreateFormRequest formRequest) {

        validateFormDefinition(formRequest);

        Form form = new Form();
        form.setName(formRequest.name());

        for (CreateFieldRequest fieldRequest : formRequest.fields()) {
            FormField field = new FormField();

            field.setName(fieldRequest.name());
            field.setLabel(fieldRequest.label());
            field.setFieldType(fieldRequest.type());
            field.setRequired(fieldRequest.required());

            if (fieldRequest.type() == FieldType.CHOICE) {
                field.setOptions(new ArrayList<>(fieldRequest.options()));

            } else {
                field.setOptions(new ArrayList<>());
            }

            form.addField(field);
        }

        Form savedForm = formRepository.save(form);
        return toResponse(savedForm);
    }

    public FormResponse getForm(Long formId) {
        Form form = findForm(formId);
        return toResponse(form);
    }

    public List<FormResponse> getAllForms() {
        return formRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FormResponse toResponse(Form form) {
        List<FieldResponse> fields = form.getFields()
                .stream()
                .map(field -> new FieldResponse(
                        field.getId(),
                        field.getName(),
                        field.getLabel(),
                        field.getFieldType(),
                        field.isRequired(),
                        List.copyOf(field.getOptions())
                ))
                .toList();

        return new FormResponse(
                form.getId(),
                form.getName(),
                fields
        );
    }

    private void validateFormDefinition(CreateFormRequest formRequest) {
        Set<String> fieldNames = new HashSet<>();

        for (CreateFieldRequest field : formRequest.fields()) {
            if (!fieldNames.add(field.name())) {
                throw new InvalidFormDefinitionException("Veldnamen moeten uniek zijn. Dubbele veld: " + field.name());
            }

            if (field.type() == FieldType.CHOICE) {
                if (field.options() == null || field.options().isEmpty()) {
                    throw new InvalidFormDefinitionException("Choice veld " + field.name() + " moet minimaal één optie hebben");
                }
            }
        }
    }

    public Form findForm(Long formId) {

        return formRepository
                .findById(formId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Formulier met id " + formId + " was niet gevonden")
                );
    }
}
