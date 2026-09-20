package nl.dignitas.evently.repository;

import nl.dignitas.evently.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
