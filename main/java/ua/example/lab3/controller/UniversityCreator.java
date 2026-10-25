package ua.example.lab3.controller;

import java.util.List;
import ua.example.lab3.model.Faculty;
import ua.example.lab3.model.Human;
import ua.example.lab3.model.University;

public class UniversityCreator {
    private FacultyCreator facultyCreator = new FacultyCreator();

    public University create(String name, Human head, List<Faculty> faculties) {
        return new University(name, head, faculties);
    }

    public FacultyCreator getFacultyCreator() {
        return facultyCreator;
    }
}
