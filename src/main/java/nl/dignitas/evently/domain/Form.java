package nl.dignitas.evently.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forms")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "form",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderColumn(name = "field_order")
    private List<FormField> fields = new ArrayList<>();

    public Form() {}

    public void addField(FormField field) {
        fields.add(field);
        field.setForm(this);
    }

    public List<FormField> getFields() {
        return fields;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
