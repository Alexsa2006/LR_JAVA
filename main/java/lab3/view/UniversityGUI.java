package lab3.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Supplier;
import com.google.gson.JsonParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import lab3.model.Department;
import lab3.model.Faculty;
import lab3.model.Group;
import lab3.model.NamedUnit;
import lab3.model.Student;
import lab3.model.University;
import lab3.persistence.JsonManager;

public class UniversityGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Supplier<University> universityFactory;
    private final JTree tree = new JTree(new DefaultMutableTreeNode("Університет"));
    private final JTextArea details = new JTextArea();
    private final JsonManager jsonManager = new JsonManager();
    private final JButton compare = new JButton("Перевірити рівність");
    private University currentUniversity;
    private University originalUniversity;

    public UniversityGUI(Supplier<University> universityFactory) {
        super("Лабораторна робота №4 - Університет. JUnit. JSON");
        this.universityFactory = java.util.Objects.requireNonNull(universityFactory);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(760, 480));
        setSize(1000, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 8));

        JPanel toolbar = new JPanel(new java.awt.GridLayout(3, 1));
        JPanel createRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JPanel jsonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton save = new JButton("Зберегти JSON");
        JButton load = new JButton("Завантажити JSON");
        jsonRow.add(save);
        jsonRow.add(load);
        jsonRow.add(compare);
        compare.setEnabled(false);
        JButton create = new JButton("Створити типовий університет");
        JButton expand = new JButton("Розгорнути все");
        JButton collapse = new JButton("Згорнути все");
        JButton info = new JButton("Інформація");
        JButton exit = new JButton("Вихід");
        createRow.add(create);
        actionRow.add(expand);
        actionRow.add(collapse);
        actionRow.add(info);
        actionRow.add(exit);
        toolbar.add(createRow);
        toolbar.add(actionRow);
        toolbar.add(jsonRow);
        add(toolbar, BorderLayout.NORTH);

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setRowHeight(26);
        tree.getSelectionModel().setSelectionMode(
                javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(event -> showSelectedObject());

        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(tree.getFont().deriveFont(Font.PLAIN, 14f));
        details.setMargin(new java.awt.Insets(12, 12, 12, 12));
        JPanel detailPanel = new JPanel(new BorderLayout(0, 8));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        detailPanel.add(new JLabel("Відомості про вибраний об'єкт"), BorderLayout.NORTH);
        detailPanel.add(new JScrollPane(details), BorderLayout.CENTER);

        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setMinimumSize(new Dimension(250, 200));
        detailPanel.setMinimumSize(new Dimension(280, 200));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, detailPanel);
        split.setResizeWeight(0.55);
        split.setDividerLocation(520);
        add(split, BorderLayout.CENTER);

        create.addActionListener(event -> createUniversity());
        save.addActionListener(event -> saveUniversity());
        load.addActionListener(event -> loadUniversity());
        compare.addActionListener(event -> showMessage(
                "Початковий та відновлений університети "
                        + (originalUniversity.equals(currentUniversity) ? "рівні." : "не рівні.")
                        + "\nequals = " + originalUniversity.equals(currentUniversity),
                JOptionPane.INFORMATION_MESSAGE));
        expand.addActionListener(event -> expandAll());
        collapse.addActionListener(event -> {
            for (int row = tree.getRowCount() - 1; row >= 0; row--) {
                tree.collapseRow(row);
            }
        });
        info.addActionListener(event -> JOptionPane.showMessageDialog(this,
                "Лабораторна робота №4. JUnit. JSON\nУніверситет, факультети, кафедри, групи та студенти."
                        + "\nЗбережіть JSON, завантажте його та перевірте рівність.",
                "Інформація", JOptionPane.INFORMATION_MESSAGE));
        exit.addActionListener(event -> dispose());
        createUniversity();
    }

    private void createUniversity() {
        currentUniversity = universityFactory.get();
        originalUniversity = jsonManager.fromJson(jsonManager.toJson(currentUniversity));
        compare.setEnabled(false);
        displayUniversity();
    }

    private void displayUniversity() {
        DefaultMutableTreeNode root = buildTree(currentUniversity);
        tree.setModel(new DefaultTreeModel(root));
        tree.setSelectionPath(new TreePath(root.getPath()));
        expandAll();
    }

    private void saveUniversity() {
        Path file = chooseJsonFile(true);
        if (file == null) { return; }
        try {
            University snapshot = jsonManager.fromJson(jsonManager.toJson(currentUniversity));
            jsonManager.saveToFile(currentUniversity, file);
            originalUniversity = snapshot;
            compare.setEnabled(false);
            showMessage("Університет збережено:\n" + file, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | JsonParseException exception) {
            showMessage("Не вдалося зберегти JSON:\n" + exception.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUniversity() {
        Path file = chooseJsonFile(false);
        if (file == null) { return; }
        try {
            University loaded = jsonManager.loadFromFile(file);
            currentUniversity = loaded;
            displayUniversity();
            compare.setEnabled(true);
            showMessage("Університет завантажено:\n" + file, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | JsonParseException exception) {
            showMessage("Не вдалося завантажити JSON:\n" + exception.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Kept separate so Swing actions can be tested without interactive dialogs. */
    protected Path chooseJsonFile(boolean save) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(save ? "Зберегти університет у JSON" : "Завантажити університет із JSON");
        chooser.setFileFilter(new FileNameExtensionFilter("JSON (*.json)", "json"));
        chooser.setAcceptAllFileFilterUsed(false);
        if (save) { chooser.setSelectedFile(new java.io.File("university.json")); }
        int result = save ? chooser.showSaveDialog(this) : chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) { return null; }
        Path file = chooser.getSelectedFile().toPath();
        if (save && !file.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json")) {
            file = file.resolveSibling(file.getFileName().toString() + ".json");
        }
        if (save && Files.exists(file) && JOptionPane.showConfirmDialog(this,
                "Файл уже існує. Перезаписати?\n" + file, "Збереження JSON",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return null;
        }
        return file;
    }

    protected void showMessage(String message, int type) {
        JOptionPane.showMessageDialog(this, message, "JUnit. JSON", type);
    }

    private void expandAll() {
        for (int row = 0; row < tree.getRowCount(); row++) {
            tree.expandRow(row);
        }
    }

    private DefaultMutableTreeNode buildTree(Object object) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(object);
        if (object instanceof University) {
            for (Faculty faculty : ((University) object).getFaculties()) {
                node.add(buildTree(faculty));
            }
        } else if (object instanceof Faculty) {
            for (Department department : ((Faculty) object).getDepartments()) {
                node.add(buildTree(department));
            }
        } else if (object instanceof Department) {
            for (Group group : ((Department) object).getGroups()) {
                node.add(buildTree(group));
            }
        } else if (object instanceof Group) {
            for (Student student : ((Group) object).getStudents()) {
                node.add(buildTree(student));
            }
        }
        return node;
    }

    private void showSelectedObject() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            details.setText("");
            return;
        }
        Object object = node.getUserObject();
        if (object instanceof Student) {
            Student student = (Student) object;
            details.setText("Тип: Студент\n\nПІБ: " + student
                    + "\n\nСтать: " + student.getSex());
        } else if (object instanceof NamedUnit) {
            NamedUnit unit = (NamedUnit) object;
            String type;
            String count;
            if (object instanceof University) {
                type = "Університет";
                count = "Факультетів: " + ((University) object).getFaculties().size();
            } else if (object instanceof Faculty) {
                type = "Факультет";
                count = "Кафедр: " + ((Faculty) object).getDepartments().size();
            } else if (object instanceof Department) {
                type = "Кафедра";
                count = "Груп: " + ((Department) object).getGroups().size();
            } else {
                type = "Група";
                count = "Студентів: " + ((Group) object).getStudents().size();
            }
            details.setText("Тип: " + type + "\n\nНазва: " + unit.getName()
                    + "\n\nКерівник: " + unit.getHead() + "\n\n" + count);
        }
        details.setCaretPosition(0);
    }
}
