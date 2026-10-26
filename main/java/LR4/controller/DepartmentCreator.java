package LR4.controller;

import java.util.ArrayList;
import java.util.List;
import LR4.model.Department;
import LR4.model.Group;
import LR4.model.Human;
import LR4.model.Sex;

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
