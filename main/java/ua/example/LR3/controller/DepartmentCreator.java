package ua.example.LR3.controller;

import java.util.List;
import ua.example.LR3.model.Department;
import ua.example.LR3.model.Group;
import ua.example.LR3.model.Human;

public class DepartmentCreator {
    private GroupCreator groupCreator = new GroupCreator();

    public Department create(String name, Human head, List<Group> groups) {
        return new Department(name, head, groups);
    }

    public GroupCreator getGroupCreator() {
        return groupCreator;
    }
}
