package lab3;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import lab3.controller.Creator;
import lab3.controller.UniversityCreator;
import lab3.model.Department;
import lab3.model.Faculty;
import lab3.model.Group;
import lab3.model.Sex;
import lab3.model.Student;
import lab3.model.University;
import lab3.view.UniversityGUI;

public class UniversityVerification {
    public static void main(String[] args) throws Exception {
        Creator<University> creator = new UniversityCreator();
        University university = creator.create();
        require(university.getFaculties().size() == 2, "Faculty count");
        int students = 0;
        for (Faculty faculty : university.getFaculties()) {
            require(faculty.getDepartments().size() == 2, "Department count");
            for (Department department : faculty.getDepartments()) {
                require(department.getGroups().size() == 2, "Group count");
                for (Group group : department.getGroups()) {
                    require(group.getStudents().size() == 2, "Student count");
                    require(group.getHead() == group.getStudents().get(0), "Group head");
                    students += group.getStudents().size();
                }
            }
        }
        require(students == 16, "Total students");
        Student student = new Student("Іван", "Іваненко", "Петрович", Sex.MALE);
        student.setFirstName("Петро");
        student.setLastName("Петренко");
        student.setPatronymic("Іванович");
        student.setSex(Sex.FEMALE);
        require(student.toString().equals("Петренко Петро Іванович"), "Human setters");
        require(student.getSex() == Sex.FEMALE, "Sex setter");
        List<Student> source = new ArrayList<Student>();
        source.add(student);
        Group group = new Group("Т-21", student, source);
        source.clear();
        require(group.getStudents().size() == 1, "Collection defensive copy");
        require(Run.createTypycalUniversity() != Run.createTypycalUniversity(), "Fresh objects");

        SwingUtilities.invokeAndWait(() -> verifyGUI());
        System.out.println("PASS: model hierarchy, creators, setters, collections and Swing actions.");
    }

    private static void verifyGUI() {
        AtomicInteger creations = new AtomicInteger();
        UniversityGUI gui = new UniversityGUI(() -> {
            creations.incrementAndGet();
            return Run.createTypycalUniversity();
        });
        try {
            gui.setVisible(true);
            JTree tree = find(gui, JTree.class);
            JTextArea details = find(gui, JTextArea.class);
            require(tree.getRowCount() == 31, "Expanded tree node count");
            require(details.getText().contains("Дніпровський університет"), "University details");
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
            DefaultMutableTreeNode node = root;
            for (int depth = 0; depth < 4; depth++) {
                node = (DefaultMutableTreeNode) node.getChildAt(0);
                tree.setSelectionPath(new TreePath(node.getPath()));
                require(details.getText().contains(node.getUserObject().toString()), "Selected details");
            }
            require(details.getText().contains("Стать: чоловіча"), "Student details");
            button(gui, "Згорнути все").doClick();
            require(tree.getRowCount() == 1, "Collapse action");
            button(gui, "Розгорнути все").doClick();
            require(tree.getRowCount() == 31, "Expand action");
            button(gui, "Створити типовий університет").doClick();
            require(creations.get() == 2, "Creation callback");
            require(root != tree.getModel().getRoot(), "Tree replacement");
            require(details.getText().contains("Тип: Університет"), "Selection after regeneration");
            gui.setSize(760, 480);
            gui.validate();
            for (Component component : components(gui)) {
                if (component instanceof JButton && ((JButton) component).getText() != null
                        && !((JButton) component).getText().isEmpty()) {
                    require(component.getWidth() >= component.getPreferredSize().width,
                            "Button label fits at minimum size");
                }
            }
            button(gui, "Вихід").doClick();
            require(!gui.isDisplayable(), "Exit action");
        } finally {
            gui.dispose();
        }
    }

    private static JButton button(Container root, String label) {
        for (Component component : components(root)) {
            if (component instanceof JButton && ((JButton) component).getText().equals(label)) {
                return (JButton) component;
            }
        }
        throw new AssertionError("Missing button: " + label);
    }

    private static <T> T find(Container root, Class<T> type) {
        for (Component component : components(root)) {
            if (type.isInstance(component)) {
                return type.cast(component);
            }
        }
        throw new AssertionError("Missing component: " + type.getName());
    }

    private static List<Component> components(Container root) {
        List<Component> result = new ArrayList<Component>();
        for (Component component : root.getComponents()) {
            result.add(component);
            if (component instanceof Container) {
                result.addAll(components((Container) component));
            }
        }
        return result;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
