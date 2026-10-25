package ua.example.lab3.view;

import ua.example.lab3.model.Department;
import ua.example.lab3.model.Faculty;
import ua.example.lab3.model.Group;
import ua.example.lab3.model.Student;
import ua.example.lab3.model.University;

public class ConsoleView {
    public void printUniversity(University university) {
        System.out.println(university);
        for (Faculty faculty : university.getFaculties()) {
            System.out.println("  " + faculty);
            for (Department department : faculty.getDepartments()) {
                System.out.println("    " + department);
                for (Group group : department.getGroups()) {
                    System.out.println("      " + group);
                    for (Student student : group.getStudents()) {
                        System.out.println("        " + student);
                    }
                }
            }
        }
    }
}
