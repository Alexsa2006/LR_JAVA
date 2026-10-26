package ua.example.LR3.controller;

import java.util.List;
import ua.example.LR3.model.Department;
import ua.example.LR3.model.Faculty;
import ua.example.LR3.model.Human;

public class FacultyCreator {
    private DepartmentCreator departmentCreator = new DepartmentCreator();

    public Faculty create(String name, Human head, List<Department> departments) {
        return new Faculty(name, head, departments);
    }

    public DepartmentCreator getDepartmentCreator() {
        return departmentCreator;
    }
}
