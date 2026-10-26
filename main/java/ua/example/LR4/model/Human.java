package ua.example.LR4.model;

import java.util.Objects;

public class Human {
    private String firstName;
    private String lastName;
    private String patronymic;
    private Sex sex;

    public Human(String firstName, String lastName, String patronymic, Sex sex) {
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setSex(sex);
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPatronymic() { return patronymic; }
    public Sex getSex() { return sex; }

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "Ім'я не може бути null");
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "Прізвище не може бути null");
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = Objects.requireNonNull(patronymic, "По батькові не може бути null");
    }

    public void setSex(Sex sex) {
        this.sex = Objects.requireNonNull(sex, "Стать не може бути null");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) { return false; }
        Human other = (Human) object;
        return Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName)
                && Objects.equals(patronymic, other.patronymic) && sex == other.sex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, sex);
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + patronymic;
    }
}
