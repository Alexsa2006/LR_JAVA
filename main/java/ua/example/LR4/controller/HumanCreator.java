package ua.example.LR4.controller;

import ua.example.LR4.model.Human;
import ua.example.LR4.model.Gender;

public class HumanCreator {
    public Human create(String firstName, String lastName, String middleName, Gender gender) {
        return new Human(firstName, lastName, middleName, gender);
    }
}
