package LR4.controller;

import LR4.model.Sex;
import LR4.model.Student;

public class StudentCreator implements Creator<Student> {
    @Override
    public Student create() {
        return create("Олександр", "Іваненко", "Іванович", Sex.MALE);
    }

    public Student create(String firstName, String lastName, String patronymic, Sex sex) {
        return new Student(firstName, lastName, patronymic, sex);
    }
}
