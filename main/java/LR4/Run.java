package LR4;

import javax.swing.SwingUtilities;
import LR4.controller.UniversityCreator;
import LR4.model.University;
import LR4.view.UniversityGUI;

public class Run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new UniversityGUI(Run::createTypycalUniversity).setVisible(true));
    }

    public static University createTypycalUniversity() {
        return new UniversityCreator().create();
    }
}
