package lab3.controller;

import java.util.ArrayList;
import java.util.List;
import lab3.model.Group;
import lab3.model.Human;
import lab3.model.Sex;
import lab3.model.Student;

public class GroupCreator implements Creator<Group> {
    private final StudentCreator studentCreator = new StudentCreator();

    @Override
    public Group create() {
        return createTypical("КН-21");
    }

    public Group createTypical(String name) {
        List<Student> students = new ArrayList<Student>();
        students.add(studentCreator.create());
        students.add(studentCreator.create("Марія", "Петренко", "Олександрівна", Sex.FEMALE));
        return create(name, students.get(0), students);
    }

    public Group create(String name, Human head, List<Student> students) {
        return new Group(name, head, students);
    }
}
