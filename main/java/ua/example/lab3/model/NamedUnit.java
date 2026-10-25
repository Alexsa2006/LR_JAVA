package ua.example.lab3.model;

import java.util.Objects;

public abstract class NamedUnit {
    private String name;
    private Human head;

    protected NamedUnit(String name, Human head) {
        this.name = name;
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public Human getHead() {
        return head;
    }

    @Override
    public String toString() {
        return name + ", керівник: " + head;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NamedUnit)) {
            return false;
        }
        NamedUnit namedUnit = (NamedUnit) o;
        return Objects.equals(name, namedUnit.name) && Objects.equals(head, namedUnit.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, head);
    }
}
