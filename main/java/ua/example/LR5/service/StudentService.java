package ua.example.LR5.service;

import java.sql.SQLException;
import java.util.List;
import ua.example.LR5.model.Student;
import ua.example.LR5.repository.StudentRepository;

public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getAllStudents() throws SQLException {
        return repository.findAll();
    }

    public List<Student> getStudentsByBirthMonth(int month) throws SQLException {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Месяц должен быть числом от 1 до 12.");
        }
        return repository.findByBirthMonth(month);
    }
}
