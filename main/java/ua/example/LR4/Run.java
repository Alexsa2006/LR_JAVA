package ua.example.LR4;

import javax.swing.SwingUtilities;
import ua.example.LR4.controller.UniversityCreator;
import ua.example.LR4.model.University;
import ua.example.LR4.view.UniversityGUI;

public class Run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new UniversityGUI(Run::createTypycalUniversity).setVisible(true));
    }

    public static University createTypycalUniversity() {
        return new UniversityCreator().create();
    }
}
