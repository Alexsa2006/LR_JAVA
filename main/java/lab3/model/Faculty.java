package lab3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Faculty extends NamedUnit {
    private final List<Department> departments;

    public Faculty(String name, Human head, List<Department> departments) {
        super(name, head);
        this.departments = new ArrayList<Department>(Objects.requireNonNull(departments));
        for (Department item : this.departments) {
            Objects.requireNonNull(item);
        }
    }

    public List<Department> getDepartments() {
        return Collections.unmodifiableList(departments);
    }

    public void addDepartment(Department item) {
        departments.add(Objects.requireNonNull(item));
    }
}
