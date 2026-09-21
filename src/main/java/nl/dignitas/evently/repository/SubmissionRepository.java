package nl.dignitas.evently.repository;

import nl.dignitas.evently.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findAllByFormIdOrderByIdAsc(Long formId);
}
