package lab3.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import lab3.model.Department;
import lab3.model.Faculty;
import lab3.model.Group;
import lab3.model.NamedUnit;
import lab3.model.Student;
import lab3.model.University;

public class UniversityGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Supplier<University> universityFactory;
    private final JTree tree = new JTree(new DefaultMutableTreeNode("Університет"));
    private final JTextArea details = new JTextArea();

    public UniversityGUI(Supplier<University> universityFactory) {
        super("Лабораторна робота №3 - Університет");
        this.universityFactory = java.util.Objects.requireNonNull(universityFactory);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(760, 480));
        setSize(1000, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 8));

        JPanel toolbar = new JPanel(new java.awt.GridLayout(2, 1));
        JPanel createRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
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
        expand.addActionListener(event -> expandAll());
        collapse.addActionListener(event -> {
            for (int row = tree.getRowCount() - 1; row >= 0; row--) {
                tree.collapseRow(row);
            }
        });
        info.addActionListener(event -> JOptionPane.showMessageDialog(this,
                "Лабораторна робота №3\nУніверситет, факультети, кафедри, групи та студенти.",
                "Інформація", JOptionPane.INFORMATION_MESSAGE));
        exit.addActionListener(event -> dispose());
        createUniversity();
    }

    private void createUniversity() {
        DefaultMutableTreeNode root = buildTree(universityFactory.get());
        tree.setModel(new DefaultTreeModel(root));
        tree.setSelectionPath(new TreePath(root.getPath()));
        expandAll();
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
