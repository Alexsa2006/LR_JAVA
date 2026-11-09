package ua.example.LR5;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import ua.example.LR5.controller.StudentController;
import ua.example.LR5.database.DatabaseConnection;
import ua.example.LR5.repository.StudentRepositoryImpl;
import ua.example.LR5.service.StudentService;
import ua.example.LR5.view.ConsoleView;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Подключение установлено: " + DatabaseConnection.getUrl()
                    + " (пользователь " + DatabaseConnection.getUser() + ").");
            StudentService service = new StudentService(new StudentRepositoryImpl());
            StudentController controller = new StudentController(service, new ConsoleView(scanner));
            controller.run();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            printDatabaseError(e);
        }
    }

    private static void printDatabaseError(SQLException error) {
        String state = error.getSQLState();
        String url = DatabaseConnection.getUrl();
        if ("28P01".equals(state)) {
            System.err.println("PostgreSQL отклонил пароль пользователя '" + DatabaseConnection.getUser()
                    + "'. Проверьте LR5_DB_PASSWORD для сервера " + url + ".");
        } else if ("3D000".equals(state)) {
            System.err.println("На сервере " + url + " нет базы university_lr5. Создайте базу в pgAdmin.");
        } else if ("42P01".equals(state)) {
            System.err.println("В university_lr5 нет таблицы students. Выполните database.sql в этой базе.");
        } else if (state != null && state.startsWith("08")) {
            System.err.println("Не удалось открыть соединение с " + url + ". Проверьте службу и порт PostgreSQL.");
        } else {
            System.err.println("Ошибка PostgreSQL/JDBC при подключении к " + url + ".");
        }
        System.err.println("SQLState: " + (state == null ? "не указан" : state));
        System.err.println("Причина: " + error.getMessage());
        SQLException next = error.getNextException();
        if (next != null && next != error) {
            System.err.println("Дополнительная причина: SQLState=" + next.getSQLState()
                    + "; " + next.getMessage());
        }
    }
}
