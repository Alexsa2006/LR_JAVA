package lab3.model;

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
    public String toString() {
        return lastName + " " + firstName + " " + patronymic;
    }
}
