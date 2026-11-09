package ua.example.LR5.controller;

import java.sql.SQLException;
import ua.example.LR5.database.DatabaseConnection;
import ua.example.LR5.service.StudentService;
import ua.example.LR5.view.ConsoleView;

public class StudentController {
    private final StudentService service;
    private final ConsoleView view;

    public StudentController(StudentService service, ConsoleView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean running = true;
        while (running) {
            view.showMenu();
            String choice = view.readLine();
            if (choice == null) {
                view.showMessage("Ввод завершён. Программа закрыта.");
                return;
            }
            try {
                switch (choice) {
                    case "1":
                        view.showStudents(service.getAllStudents());
                        break;
                    case "2":
                        findByMonth();
                        break;
                    case "3":
                        running = false;
                        view.showMessage("Работа программы завершена.");
                        break;
                    default:
                        view.showMessage("Выберите пункт меню: 1, 2 или 3.");
                }
            } catch (SQLException e) {
                view.showMessage(databaseError(e));
            }
        }
    }

    private void findByMonth() throws SQLException {
        view.showMessage("Введите номер месяца (1–12):");
        String input = view.readLine();
        if (input == null) {
            view.showMessage("Ввод завершён. Программа закрыта.");
            return;
        }
        try {
            int month = Integer.parseInt(input);
            view.showStudents(service.getStudentsByBirthMonth(month));
        } catch (NumberFormatException e) {
            view.showMessage("Некорректный ввод. Введите целое число от 1 до 12.");
        } catch (IllegalArgumentException e) {
            view.showMessage(e.getMessage());
        }
    }

    private String databaseError(SQLException error) {
        String state = error.getSQLState();
        String hint;
        if ("28P01".equals(state)) {
            hint = "PostgreSQL отклонил пароль пользователя '" + DatabaseConnection.getUser() + "'.";
        } else if ("3D000".equals(state)) {
            hint = "База university_lr5 не найдена на сервере " + DatabaseConnection.getUrl() + ".";
        } else if ("42P01".equals(state)) {
            hint = "Таблица students не найдена. Выполните database.sql в university_lr5.";
        } else if (state != null && state.startsWith("08")) {
            hint = "Нет соединения с " + DatabaseConnection.getUrl() + ". Проверьте сервер и порт.";
        } else {
            hint = "Ошибка SQL-запроса к PostgreSQL.";
        }
        return hint + " SQLState=" + (state == null ? "не указан" : state)
                + "; причина: " + error.getMessage();
    }
}
