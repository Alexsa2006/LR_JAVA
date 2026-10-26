package ua.example.LR4;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import ua.example.LR4.model.University;
import ua.example.LR4.persistence.JsonManager;
import ua.example.LR4.view.UniversityGUI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class UniversityGuiTest {
    @TempDir Path temporaryDirectory;

    @Test
    void previousUniversityVerificationStillPasses() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Swing requires a graphical desktop");
        UniversityVerification.main(new String[0]);
    }

    @Test
    void jsonButtonsUpdateTreeCompareDataAndRecoverFromErrors() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Swing requires a graphical desktop");
        Path saved = temporaryDirectory.resolve("saved.json");
        Path different = temporaryDirectory.resolve("different.json");
        Path invalid = temporaryDirectory.resolve("invalid.json");
        University changed = Run.createTypycalUniversity();
        changed.setName("Завантажений університет");
        new JsonManager().saveToFile(changed, different);
        Files.write(invalid, "{}".getBytes(StandardCharsets.UTF_8));
        SwingUtilities.invokeAndWait(() -> {
            TestGUI gui = new TestGUI();
            try {
                gui.setVisible(true);
                BufferedImage preview = new BufferedImage(gui.getWidth(), gui.getHeight(), BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D graphics = preview.createGraphics();
                try {
                    gui.paint(graphics);
                } finally {
                    graphics.dispose();
                }
                try {
                    ImageIO.write(preview, "png", new java.io.File("target/gui-preview.png"));
                } catch (java.io.IOException exception) {
                    throw new AssertionError("Cannot render GUI preview", exception);
                }
                JTree tree = find(gui, JTree.class);
                JTextArea details = find(gui, JTextArea.class);
                assertFalse(button(gui, "Перевірити рівність").isEnabled());
                Object originalRoot = tree.getModel().getRoot();
                // Cancelling a chooser must leave the model and reference unchanged.
                button(gui, "Зберегти JSON").doClick();
                button(gui, "Завантажити JSON").doClick();
                assertSame(originalRoot, tree.getModel().getRoot());
                gui.file = saved;
                button(gui, "Зберегти JSON").doClick();
                assertTrue(Files.exists(saved));
                assertEquals(JOptionPane.INFORMATION_MESSAGE, gui.messageType);
                button(gui, "Завантажити JSON").doClick();
                assertNotSame(originalRoot, tree.getModel().getRoot());
                assertEquals(31, tree.getRowCount());
                assertTrue(button(gui, "Перевірити рівність").isEnabled());
                button(gui, "Перевірити рівність").doClick();
                assertTrue(gui.message.contains("equals = true"));
                gui.file = different;
                button(gui, "Завантажити JSON").doClick();
                assertTrue(details.getText().contains("Завантажений університет"));
                button(gui, "Перевірити рівність").doClick();
                assertTrue(gui.message.contains("equals = false"));
                Object loadedRoot = tree.getModel().getRoot();
                gui.file = invalid;
                button(gui, "Завантажити JSON").doClick();
                assertEquals(JOptionPane.ERROR_MESSAGE, gui.messageType);
                assertSame(loadedRoot, tree.getModel().getRoot());
                gui.file = temporaryDirectory.resolve("missing.json");
                button(gui, "Завантажити JSON").doClick();
                assertEquals(JOptionPane.ERROR_MESSAGE, gui.messageType);
                assertSame(loadedRoot, tree.getModel().getRoot());
                gui.file = temporaryDirectory;
                button(gui, "Зберегти JSON").doClick();
                assertEquals(JOptionPane.ERROR_MESSAGE, gui.messageType);
                button(gui, "Перевірити рівність").doClick();
                assertTrue(gui.message.contains("equals = false"));
                button(gui, "Створити типовий університет").doClick();
                assertFalse(button(gui, "Перевірити рівність").isEnabled());
                assertEquals(31, tree.getRowCount());
                assertEquals(Run.createTypycalUniversity(),
                        ((DefaultMutableTreeNode) tree.getModel().getRoot()).getUserObject());
            } finally {
                gui.dispose();
            }
        });
    }

    private static class TestGUI extends UniversityGUI {
        private Path file;
        private String message;
        private int messageType;

        TestGUI() { super(Run::createTypycalUniversity); }

        @Override protected Path chooseJsonFile(boolean save) { return file; }

        @Override protected void showMessage(String text, int type) {
            message = text;
            messageType = type;
        }
    }

    private static JButton button(Container root, String text) {
        for (Component component : components(root)) {
            if (component instanceof JButton && text.equals(((JButton) component).getText())) {
                return (JButton) component;
            }
        }
        throw new AssertionError("Missing button: " + text);
    }

    private static <T> T find(Container root, Class<T> type) {
        for (Component component : components(root)) {
            if (type.isInstance(component)) { return type.cast(component); }
        }
        throw new AssertionError("Missing component: " + type);
    }

    private static List<Component> components(Container root) {
        List<Component> result = new ArrayList<>();
        for (Component component : root.getComponents()) {
            result.add(component);
            if (component instanceof Container) { result.addAll(components((Container) component)); }
        }
        return result;
    }
}
