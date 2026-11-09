package ua.example.LR5.repository;

import java.sql.SQLException;
import java.util.List;
import ua.example.LR5.model.Student;

public interface StudentRepository {
    List<Student> findAll() throws SQLException;

    /** The month is validated by the service and bound as a SQL parameter. */
    List<Student> findByBirthMonth(int month) throws SQLException;
}
