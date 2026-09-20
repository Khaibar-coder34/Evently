package nl.dignitas.evently.repository;

import nl.dignitas.evently.domain.Form;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form, Long> {
}
