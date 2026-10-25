package ua.example.lab3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Faculty extends NamedUnit {
    private List<Department> departments;

    public Faculty(String name, Human head, List<Department> departments) {
        super(name, head);
        this.departments = new ArrayList<Department>(departments);
    }

    public List<Department> getDepartments() {
        return Collections.unmodifiableList(departments);
    }

    @Override
    public String toString() {
        return super.toString() + ", кафедр: " + departments.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Faculty)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Faculty faculty = (Faculty) o;
        return Objects.equals(departments, faculty.departments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), departments);
    }
}
