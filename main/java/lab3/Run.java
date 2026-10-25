package lab3;

import javax.swing.SwingUtilities;
import lab3.controller.UniversityCreator;
import lab3.model.University;
import lab3.view.UniversityGUI;

public class Run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new UniversityGUI(Run::createTypycalUniversity).setVisible(true));
    }

    public static University createTypycalUniversity() {
        return new UniversityCreator().create();
    }
}
