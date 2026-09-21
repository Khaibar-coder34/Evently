package nl.dignitas.evently.controller;

import jakarta.validation.Valid;
import nl.dignitas.evently.dto.CreateSubmissionRequest;
import nl.dignitas.evently.dto.SubmissionResponse;
import nl.dignitas.evently.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/{formId}/submissions")
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionResponse createSubmission(@PathVariable Long formId, @Valid @RequestBody CreateSubmissionRequest request) {
        return submissionService.createSubmission(formId, request);
    }

    @GetMapping("/{formId}/submissions")
    public List<SubmissionResponse> getSubmissions(@PathVariable Long formId) {
        return submissionService.getSubmissions(formId);
    }
}
