package ua.example.lab3.controller;

import ua.example.lab3.model.Human;
import ua.example.lab3.model.Gender;

public class HumanCreator {
    public Human create(String firstName, String lastName, String middleName, Gender gender) {
        return new Human(firstName, lastName, middleName, gender);
    }
}
