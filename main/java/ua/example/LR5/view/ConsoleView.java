package ua.example.LR5.view;

import java.util.List;
import java.util.Scanner;
import ua.example.LR5.model.Student;

public class ConsoleView {
    private final Scanner scanner;

    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu() {
        System.out.println("\n========== УЧЕТ СТУДЕНТОВ ==========");
        System.out.println("1. Показать всех студентов.");
        System.out.println("2. Найти студентов по месяцу рождения.");
        System.out.println("3. Выход.");
        System.out.print("Выберите действие: ");
    }

    public String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }

    public void showStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("В этом месяце нет студентов с днём рождения.");
            return;
        }
        System.out.println("ID | Фамилия | Имя | Отчество | Дата рождения | Номер зачётной книжки");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
