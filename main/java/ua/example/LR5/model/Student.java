package ua.example.LR5.model;

import java.time.LocalDate;
import java.util.Objects;

public class Student {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDate birthDate;
    private String recordBookNumber;

    public Student() { }

    public Student(int id, String surname, String name, String patronymic,
                   LocalDate birthDate, String recordBookNumber) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.recordBookNumber = recordBookNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPatronymic() { return patronymic; }
    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getRecordBookNumber() { return recordBookNumber; }
    public void setRecordBookNumber(String recordBookNumber) { this.recordBookNumber = recordBookNumber; }

    @Override
    public String toString() {
        return id + " | " + surname + " | " + name + " | " + patronymic
                + " | " + birthDate + " | " + recordBookNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Student)) return false;
        Student student = (Student) other;
        return id == student.id
                && Objects.equals(surname, student.surname)
                && Objects.equals(name, student.name)
                && Objects.equals(patronymic, student.patronymic)
                && Objects.equals(birthDate, student.birthDate)
                && Objects.equals(recordBookNumber, student.recordBookNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name, patronymic, birthDate, recordBookNumber);
    }
}
