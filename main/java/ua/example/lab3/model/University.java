package ua.example.lab3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class University extends NamedUnit {
    private List<Faculty> faculties;

    public University(String name, Human head, List<Faculty> faculties) {
        super(name, head);
        this.faculties = new ArrayList<Faculty>(faculties);
    }

    public List<Faculty> getFaculties() {
        return Collections.unmodifiableList(faculties);
    }

    @Override
    public String toString() {
        return super.toString() + ", факультетів: " + faculties.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof University)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        University that = (University) o;
        return Objects.equals(faculties, that.faculties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), faculties);
    }
}
