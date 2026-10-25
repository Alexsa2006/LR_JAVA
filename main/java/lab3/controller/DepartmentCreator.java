package lab3.controller;

import java.util.ArrayList;
import java.util.List;
import lab3.model.Department;
import lab3.model.Group;
import lab3.model.Human;
import lab3.model.Sex;

public class DepartmentCreator implements Creator<Department> {
    private final GroupCreator groupCreator = new GroupCreator();

    @Override
    public Department create() {
        return createTypical("Кафедра комп'ютерних наук", "КН");
    }

    public Department createTypical(String name, String groupPrefix) {
        List<Group> groups = new ArrayList<Group>();
        groups.add(groupCreator.createTypical(groupPrefix + "-21"));
        groups.add(groupCreator.createTypical(groupPrefix + "-22"));
        Human head = new Human("Олена", "Коваленко", "Сергіївна", Sex.FEMALE);
        return create(name, head, groups);
    }

    public Department create(String name, Human head, List<Group> groups) {
        return new Department(name, head, groups);
    }
}
