package LR4.controller;

import java.util.ArrayList;
import java.util.List;
import LR4.model.Faculty;
import LR4.model.Human;
import LR4.model.Sex;
import LR4.model.University;

public class UniversityCreator implements Creator<University> {
    private final FacultyCreator facultyCreator = new FacultyCreator();

    @Override
    public University create() {
        List<Faculty> faculties = new ArrayList<Faculty>();
        faculties.add(facultyCreator.create());
        faculties.add(facultyCreator.createTypical("Економічний факультет",
                "Кафедра економіки", "ЕК", "Кафедра фінансів", "ФН"));
        Human rector = new Human("Іван", "Мельник", "Миколайович", Sex.MALE);
        return create("Дніпровський університет", rector, faculties);
    }

    public University create(String name, Human head, List<Faculty> faculties) {
        return new University(name, head, faculties);
    }
}
