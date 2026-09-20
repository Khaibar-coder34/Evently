package nl.dignitas.evently.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    // Met annotiatie wordt deze map als JSON naar de database wordt geschreven
    @JdbcTypeCode(SqlTypes.JSON)
    // Met deze kan postgres JSON als echte gestructureerde data bewaren ipv als één gewone tekststring.
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> answers = new LinkedHashMap<>();

    public Submission() {}

    public Long getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public Map<String, Object> getAnswers() {
        return answers;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setAnswers(Map<String, Object> answers) {
        this.answers = answers;
    }
}
