package lab3.controller;

import java.util.ArrayList;
import java.util.List;
import lab3.model.Department;
import lab3.model.Faculty;
import lab3.model.Human;
import lab3.model.Sex;

public class FacultyCreator implements Creator<Faculty> {
    private final DepartmentCreator departmentCreator = new DepartmentCreator();

    @Override
    public Faculty create() {
        return createTypical("Факультет інформаційних технологій",
                "Кафедра комп'ютерних наук", "КН",
                "Кафедра програмної інженерії", "ПІ");
    }

    public Faculty createTypical(String name, String firstDepartment, String firstPrefix,
            String secondDepartment, String secondPrefix) {
        List<Department> departments = new ArrayList<Department>();
        departments.add(departmentCreator.createTypical(firstDepartment, firstPrefix));
        departments.add(departmentCreator.createTypical(secondDepartment, secondPrefix));
        Human dean = new Human("Андрій", "Шевченко", "Петрович", Sex.MALE);
        return create(name, dean, departments);
    }

    public Faculty create(String name, Human head, List<Department> departments) {
        return new Faculty(name, head, departments);
    }
}
