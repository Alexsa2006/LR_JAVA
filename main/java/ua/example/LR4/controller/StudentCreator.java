package ua.example.LR4.controller;

import ua.example.LR4.model.Gender;
import ua.example.LR4.model.Student;

public class StudentCreator {
    public Student create(String firstName, String lastName, String middleName, Gender gender) {
        return new Student(firstName, lastName, middleName, gender);
    }
}
