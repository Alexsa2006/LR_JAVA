package ua.example.LR5.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.example.LR5.database.DatabaseConnection;
import ua.example.LR5.model.Student;

public class StudentRepositoryImpl implements StudentRepository {
    private static final String COLUMNS =
            "id, surname, name, patronymic, birth_date, record_book_number";

    @Override
    public List<Student> findAll() throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM students ORDER BY surname, name";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            return mapRows(result);
        }
    }

    @Override
    public List<Student> findByBirthMonth(int month) throws SQLException {
        String sql = "SELECT " + COLUMNS
                + " FROM students WHERE EXTRACT(MONTH FROM birth_date) = ?"
                + " ORDER BY surname, name";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, month);
            try (ResultSet result = statement.executeQuery()) {
                return mapRows(result);
            }
        }
    }

    private List<Student> mapRows(ResultSet result) throws SQLException {
        List<Student> students = new ArrayList<Student>();
        while (result.next()) {
            Date date = result.getDate("birth_date");
            students.add(new Student(
                    result.getInt("id"),
                    result.getString("surname"),
                    result.getString("name"),
                    result.getString("patronymic"),
                    date.toLocalDate(),
                    result.getString("record_book_number")));
        }
        return students;
    }
}
