package ua.example.LR3.controller;

import java.util.List;
import ua.example.LR3.model.Group;
import ua.example.LR3.model.Human;
import ua.example.LR3.model.Student;

public class GroupCreator {
    public Group create(String name, Human head, List<Student> students) {
        return new Group(name, head, students);
    }
}
