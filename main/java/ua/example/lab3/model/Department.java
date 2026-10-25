package ua.example.lab3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Department extends NamedUnit {
    private List<Group> groups;

    public Department(String name, Human head, List<Group> groups) {
        super(name, head);
        this.groups = new ArrayList<Group>(groups);
    }

    public List<Group> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    @Override
    public String toString() {
        return super.toString() + ", груп: " + groups.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Department that = (Department) o;
        return Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), groups);
    }
}
