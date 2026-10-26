package LR4.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Group extends NamedUnit {
    private final List<Student> students;

    public Group(String name, Human head, List<Student> students) {
        super(name, head);
        this.students = new ArrayList<Student>(Objects.requireNonNull(students));
        for (Student item : this.students) {
            Objects.requireNonNull(item);
        }
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(Student item) {
        students.add(Objects.requireNonNull(item));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!super.equals(object)) { return false; }
        Group other = (Group) object;
        return Objects.equals(students, other.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), students);
    }
}
