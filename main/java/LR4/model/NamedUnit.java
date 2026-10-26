package LR4.model;

import java.util.Objects;

public abstract class NamedUnit {
    private String name;
    private Human head;

    protected NamedUnit(String name, Human head) {
        setName(name);
        setHead(head);
    }

    public String getName() { return name; }
    public Human getHead() { return head; }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Назва не може бути null");
    }

    public void setHead(Human head) {
        this.head = Objects.requireNonNull(head, "Керівник не може бути null");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) { return false; }
        NamedUnit other = (NamedUnit) object;
        return Objects.equals(name, other.name) && Objects.equals(head, other.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, head);
    }

    @Override
    public String toString() {
        return name;
    }
}
