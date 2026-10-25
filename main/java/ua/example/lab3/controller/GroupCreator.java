package ua.example.lab3.controller;

import java.util.List;
import ua.example.lab3.model.Group;
import ua.example.lab3.model.Human;
import ua.example.lab3.model.Student;

public class GroupCreator {
    public Group create(String name, Human head, List<Student> students) {
        return new Group(name, head, students);
    }
}
