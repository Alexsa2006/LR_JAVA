package ua.example.lab3.controller;

import java.util.List;
import ua.example.lab3.model.Department;
import ua.example.lab3.model.Group;
import ua.example.lab3.model.Human;

public class DepartmentCreator {
    private GroupCreator groupCreator = new GroupCreator();

    public Department create(String name, Human head, List<Group> groups) {
        return new Department(name, head, groups);
    }

    public GroupCreator getGroupCreator() {
        return groupCreator;
    }
}
