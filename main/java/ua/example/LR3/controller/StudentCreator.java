package ua.example.LR3.controller;

import ua.example.LR3.model.Gender;
import ua.example.LR3.model.Student;

public class StudentCreator {
    public Student create(String firstName, String lastName, String middleName, Gender gender) {
        return new Student(firstName, lastName, middleName, gender);
    }
}
