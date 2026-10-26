package ua.example.LR4.controller;

import java.util.List;
import ua.example.LR4.model.Faculty;
import ua.example.LR4.model.Human;
import ua.example.LR4.model.University;

public class UniversityCreator {
    private FacultyCreator facultyCreator = new FacultyCreator();

    public University create(String name, Human head, List<Faculty> faculties) {
        return new University(name, head, faculties);
    }

    public FacultyCreator getFacultyCreator() {
        return facultyCreator;
    }
}
