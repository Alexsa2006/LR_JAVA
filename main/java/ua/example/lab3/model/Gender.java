package ua.example.lab3.model;

public enum Gender {
    MALE("чоловіча"),
    FEMALE("жіноча");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
