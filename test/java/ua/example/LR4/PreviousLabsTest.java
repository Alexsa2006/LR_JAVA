package ua.example.LR4;

import org.junit.jupiter.api.Test;
import ua.example.LR2.MatrixApp;
import ua.example.LR3.model.Department;
import ua.example.LR3.model.Faculty;
import ua.example.LR3.model.Group;
import ua.example.LR3.model.University;
import static org.junit.jupiter.api.Assertions.*;

class PreviousLabsTest {
    @Test
    void geometricMeanRejectsNegativeValuesRegardlessOfZeroPosition() {
        assertFalse(MatrixApp.calculateGeometricAverage(new int[][] { { 0, -1 } }).isPresent());
        assertFalse(MatrixApp.calculateGeometricAverage(new int[][] { { -1, 0 } }).isPresent());
        assertFalse(MatrixApp.calculateGeometricAverage(new int[][] { { 0, 1 }, { 2, -1 } }).isPresent());
        assertEquals(0.0, MatrixApp.calculateGeometricAverage(new int[][] { { 0, 4 } }).getAsDouble());
        assertEquals(4.0, MatrixApp.calculateGeometricAverage(new int[][] { { 2, 8 } }).getAsDouble(), 1e-10);
    }

    @Test
    void matrixStatisticsHandleLargeAndNegativeIntegers() {
        int[][] matrix = { { -5, 2 }, { 3, 8 } };
        assertEquals(-5, MatrixApp.findMin(matrix));
        assertEquals(8, MatrixApp.findMax(matrix));
        assertEquals(2.0, MatrixApp.calculateArithmeticAverage(matrix));
        assertEquals((double) Integer.MAX_VALUE, MatrixApp.calculateArithmeticAverage(
                new int[][] { { Integer.MAX_VALUE, Integer.MAX_VALUE } }));
    }

    @Test
    void previousConsoleUniversityStillCreatesItsCompleteHierarchy() {
        University university = ua.example.LR3.Run.createTypycalUniversity();
        University same = ua.example.LR3.Run.createTypycalUniversity();
        assertEquals(university, same);
        assertEquals(university.hashCode(), same.hashCode());
        assertEquals(2, university.getFaculties().size());
        int students = 0;
        for (Faculty faculty : university.getFaculties()) {
            assertEquals(2, faculty.getDepartments().size());
            for (Department department : faculty.getDepartments()) {
                assertEquals(2, department.getGroups().size());
                for (Group group : department.getGroups()) {
                    assertEquals(2, group.getStudents().size());
                    assertSame(group.getStudents().get(0), group.getHead());
                    students += group.getStudents().size();
                }
            }
        }
        assertEquals(16, students);
    }
}
