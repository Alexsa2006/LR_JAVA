package ua.example.lab3;

import java.util.ArrayList;
import java.util.List;
import ua.example.lab3.controller.DepartmentCreator;
import ua.example.lab3.controller.FacultyCreator;
import ua.example.lab3.controller.GroupCreator;
import ua.example.lab3.controller.HumanCreator;
import ua.example.lab3.controller.StudentCreator;
import ua.example.lab3.controller.UniversityCreator;
import ua.example.lab3.model.Department;
import ua.example.lab3.model.Faculty;
import ua.example.lab3.model.Group;
import ua.example.lab3.model.Human;
import ua.example.lab3.model.Gender;
import ua.example.lab3.model.Student;
import ua.example.lab3.model.University;
import ua.example.lab3.view.ConsoleView;

public class Run {
    private static final HumanCreator HUMAN_CREATOR = new HumanCreator();
    private static final StudentCreator STUDENT_CREATOR = new StudentCreator();
    private static final UniversityCreator UNIVERSITY_CREATOR = new UniversityCreator();

    public static void main(String[] args) {
        University university = createTypycalUniversity();
        new ConsoleView().printUniversity(university);
    }

    public static University createTypycalUniversity() {
        Human rector = HUMAN_CREATOR.create("Іван", "Ректоренко", "Петрович", Gender.MALE);
        List<Faculty> faculties = new ArrayList<Faculty>();
        faculties.add(createFaculty("Факультет комп'ютерних наук", "КН"));
        faculties.add(createFaculty("Економічний факультет", "ЕК"));
        return UNIVERSITY_CREATOR.create("Навчальний національний університет", rector, faculties);
    }

    private static Faculty createFaculty(String facultyName, String prefix) {
        FacultyCreator facultyCreator = UNIVERSITY_CREATOR.getFacultyCreator();
        DepartmentCreator departmentCreator = facultyCreator.getDepartmentCreator();

        Human dean = HUMAN_CREATOR.create("Олександр", "Деканенко", "Іванович", Gender.MALE);
        List<Department> departments = new ArrayList<Department>();
        departments.add(createDepartment(departmentCreator, "Кафедра програмної інженерії", prefix + "ПІ"));
        departments.add(createDepartment(departmentCreator, "Кафедра інформаційних систем", prefix + "ІС"));
        return facultyCreator.create(facultyName, dean, departments);
    }

    private static Department createDepartment(DepartmentCreator departmentCreator, String departmentName, String prefix) {
        GroupCreator groupCreator = departmentCreator.getGroupCreator();
        Human head = HUMAN_CREATOR.create("Олена", "Кафедренко", "Олександрівна", Gender.FEMALE);
        List<Group> groups = new ArrayList<Group>();
        groups.add(createGroup(groupCreator, prefix + "-101"));
        groups.add(createGroup(groupCreator, prefix + "-102"));
        return departmentCreator.create(departmentName, head, groups);
    }

    private static Group createGroup(GroupCreator groupCreator, String groupName) {
        List<Student> students = new ArrayList<Student>();
        students.add(STUDENT_CREATOR.create("Анна", "Коваленко", "Сергіївна", Gender.FEMALE));
        students.add(STUDENT_CREATOR.create("Петро", "Мельничук", "Іванович", Gender.MALE));
        Human elder = students.get(0);
        return groupCreator.create(groupName, elder, students);
    }
}
