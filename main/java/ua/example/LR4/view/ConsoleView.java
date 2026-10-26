package ua.example.LR4.view;

import ua.example.LR4.model.Department;
import ua.example.LR4.model.Faculty;
import ua.example.LR4.model.Group;
import ua.example.LR4.model.Student;
import ua.example.LR4.model.University;

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
