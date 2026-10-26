package ua.example.LR4.model;

public enum Sex {
    MALE("чоловіча"),
    FEMALE("жіноча");

    private final String label;

    Sex(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
