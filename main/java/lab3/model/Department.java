package lab3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Department extends NamedUnit {
    private final List<Group> groups;

    public Department(String name, Human head, List<Group> groups) {
        super(name, head);
        this.groups = new ArrayList<Group>(Objects.requireNonNull(groups));
        for (Group item : this.groups) {
            Objects.requireNonNull(item);
        }
    }

    public List<Group> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    public void addGroup(Group item) {
        groups.add(Objects.requireNonNull(item));
    }
}
