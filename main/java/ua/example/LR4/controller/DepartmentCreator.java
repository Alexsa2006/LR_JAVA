package ua.example.LR4.controller;

import java.util.List;
import ua.example.LR4.model.Department;
import ua.example.LR4.model.Group;
import ua.example.LR4.model.Human;

public class DepartmentCreator {
    private GroupCreator groupCreator = new GroupCreator();

    public Department create(String name, Human head, List<Group> groups) {
        return new Department(name, head, groups);
    }

    public GroupCreator getGroupCreator() {
        return groupCreator;
    }
}
