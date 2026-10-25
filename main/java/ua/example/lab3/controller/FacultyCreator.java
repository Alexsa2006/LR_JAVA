package ua.example.lab3.controller;

import java.util.List;
import ua.example.lab3.model.Department;
import ua.example.lab3.model.Faculty;
import ua.example.lab3.model.Human;

public class FacultyCreator {
    private DepartmentCreator departmentCreator = new DepartmentCreator();

    public Faculty create(String name, Human head, List<Department> departments) {
        return new Faculty(name, head, departments);
    }

    public DepartmentCreator getDepartmentCreator() {
        return departmentCreator;
    }
}
