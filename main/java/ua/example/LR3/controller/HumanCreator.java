package ua.example.LR3.controller;

import ua.example.LR3.model.Human;
import ua.example.LR3.model.Gender;

public class HumanCreator {
    public Human create(String firstName, String lastName, String middleName, Gender gender) {
        return new Human(firstName, lastName, middleName, gender);
    }
}
