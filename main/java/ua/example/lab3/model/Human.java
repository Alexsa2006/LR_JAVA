package ua.example.lab3.model;

import java.util.Objects;

public class Human {
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;

    public Human(String firstName, String lastName, String middleName, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName + " (" + gender + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Human)) {
            return false;
        }
        Human human = (Human) o;
        return Objects.equals(firstName, human.firstName)
                && Objects.equals(lastName, human.lastName)
                && Objects.equals(middleName, human.middleName)
                && gender == human.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, middleName, gender);
    }
}
