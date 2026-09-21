package nl.dignitas.evently.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_fields")
public class FormField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType fieldType;

    @Column(nullable = false)
    private boolean required;

    @ElementCollection
    @CollectionTable(
            name = "form_field_options",
            joinColumns = @JoinColumn(name = "form_field_id")
    )
    @Column(name = "option_value")
    @OrderColumn(name = "option_order")
    private List<String> options = new ArrayList<>();

    public FormField() {}

    public Long getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public String getName() {
        return name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isRequired() {
        return required;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}