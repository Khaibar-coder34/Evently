package nl.dignitas.evently.service;

import jakarta.validation.constraints.NotNull;
import nl.dignitas.evently.domain.Form;
import nl.dignitas.evently.domain.FormField;
import nl.dignitas.evently.domain.Submission;
import nl.dignitas.evently.dto.CreateSubmissionRequest;
import nl.dignitas.evently.dto.SubmissionResponse;
import nl.dignitas.evently.exception.InvalidSubmissionException;
import nl.dignitas.evently.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.View;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Transactional(readOnly = true)
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final FormService formService;
    private final View error;

    public SubmissionService(SubmissionRepository submissionRepository, FormService formService, View error) {
        this.submissionRepository = submissionRepository;
        this.formService = formService;
        this.error = error;
    }

    @Transactional
    public SubmissionResponse createSubmission(Long formId, CreateSubmissionRequest request) {
        Form form = formService.findForm(formId);

        Map<String, String > errors = validateAnswers(form, request.answers());

        if (!errors.isEmpty()) {
            throw new InvalidSubmissionException(errors);
        }

        Submission submission = new Submission();
        submission.setForm(form);

        submission.setAnswers(new LinkedHashMap<>(request.answers()));
        Submission savedSubmission = submissionRepository.save(submission);

        return toResponse(savedSubmission);
    }

    private SubmissionResponse toResponse(Submission savedSubmission) {
        return new SubmissionResponse(
                savedSubmission.getId(),
                savedSubmission.getForm().getId(),
                new LinkedHashMap<>(savedSubmission.getAnswers())
        );
    }

    private Map<String, String> validateAnswers(Form form, @NotNull(message = "Antwoorden zijn verplicht") Map<String, Object> answers) {

        Map<String, FormField> fieldsByName = new LinkedHashMap<>();

        for (FormField field : form.getFields()) {
            fieldsByName.put(field.getName(), field);
        }

        Map<String, String> errors = new LinkedHashMap<>();

        // Controleren of de gebruiker fields meestuurt die helemaal niet bestaan bijvoorbeeld 'imail' ipv 'email'.
        for (String answerName  : answers.keySet()) {
            if (!fieldsByName.containsKey(answerName)) {
                errors.put(answerName, "Onbekend veld voor dit formulier");
            }
        }

        // Nu loopen door alle echte velden van het formulier
        for (FormField field : form.getFields()) {

            Object value = answers.get(field.getName());
            boolean fieldWasSent = answers.containsKey(field.getName());

            // Validatie voor de verplichtheid
            if (field.isRequired() && (!fieldWasSent || isEmpty(value))) {
                errors.put(field.getName(), "Dit veld is verplicht");
            }

            // Een optioneel veld dat niet is ingevuld hoef niet gevalideerd te worden
            if (!fieldWasSent || isEmpty(value)) {
                continue;
            }

            // Type-speciefieke validatie
            switch (field.getFieldType()) {
                case TEXT -> validateText(field, value, errors);
                case EMAIL -> validateEmail(field, value, errors);
                case NUMBER -> validateNumber(field, value, errors);
                case DATE -> validateDate(field, value, errors);
                case CHOICE -> validateChoice(field, value, errors);
            }
        }

        return errors;
    }

    private void validateChoice(FormField field, Object value, Map<String, String> errors) {
        if (!(value instanceof String choice)) {
            errors.put(field.getName(), "Moet één van de: " + String.join(",", field.getOptions() + " zijn"));
            return;
        }
        if (!field.getOptions().contains(choice)) {
            errors.put(field.getName(), "Moet één van de: " + String.join(",", field.getOptions() + " zijn"));
        }
    }

    private void validateDate(FormField field, Object value, Map<String, String> errors) {
        if (!(value instanceof String dateValue)) {
            errors.put(field.getName(), "Moet een datum zijn in yyyy-mm-dd formaat");
            return;
        }
        try {
            LocalDate.parse(dateValue);
        } catch (DateTimeParseException exception) {
            errors.put(field.getName(), "Moet een datum zijn in yyyy-mm-dd formaat");
        }
    }

    private void validateNumber(FormField field, Object value, Map<String, String> errors) {
        if (!(value instanceof Number)) {
            errors.put(field.getName(), "Moet een nummer zijn");
        }
    }

    private void validateEmail(FormField field, Object value, Map<String, String> errors) {
        if (!(value instanceof String email)) {
            errors.put(field.getName(), "Moet een valide email adres zijn");
            return;
        }
        boolean validEmail = email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

        if (!validEmail) {
            errors.put(field.getName(), "Moet een valide emai adres zijn");
        }
    }

    private void validateText(FormField field, Object value, Map<String, String> errors) {
        if (!(value instanceof String)) {
            errors.put(field.getName(), "Moet tekst zijn");
        }
    }
}
