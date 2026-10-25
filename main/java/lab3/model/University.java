package lab3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class University extends NamedUnit {
    private final List<Faculty> faculties;

    public University(String name, Human head, List<Faculty> faculties) {
        super(name, head);
        this.faculties = new ArrayList<Faculty>(Objects.requireNonNull(faculties));
        for (Faculty item : this.faculties) {
            Objects.requireNonNull(item);
        }
    }

    public List<Faculty> getFaculties() {
        return Collections.unmodifiableList(faculties);
    }

    public void addFaculty(Faculty item) {
        faculties.add(Objects.requireNonNull(item));
    }
}
