package lab3;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lab3.model.Department;
import lab3.model.Faculty;
import lab3.model.Group;
import lab3.model.Human;
import lab3.model.Sex;
import lab3.model.Student;
import lab3.model.University;
import lab3.persistence.JsonManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class UniversityJsonTest {
    @TempDir Path temporaryDirectory;
    private final JsonManager manager = new JsonManager();

    @Test
    void universitySurvivesJsonFileRoundTrip() throws IOException {
        University oldUniversity = Run.createTypycalUniversity();
        assertEquals(2, oldUniversity.getFaculties().size());
        int students = 0;
        for (Faculty faculty : oldUniversity.getFaculties()) {
            assertEquals(2, faculty.getDepartments().size());
            for (Department department : faculty.getDepartments()) {
                assertEquals(2, department.getGroups().size());
                for (Group group : department.getGroups()) {
                    assertEquals(2, group.getStudents().size());
                    students += group.getStudents().size();
                }
            }
        }
        assertEquals(16, students);
        Path file = temporaryDirectory.resolve("university.json");
        manager.saveToFile(oldUniversity, file);
        String json = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        assertTrue(json.contains("Дніпровський університет"));
        University newUniversity = manager.fromJson(json);
        assertEquals(oldUniversity, newUniversity);
        assertTrue(oldUniversity.equals(newUniversity));
        assertEquals(newUniversity, oldUniversity);
        assertEquals(oldUniversity.hashCode(), newUniversity.hashCode());
        assertNotSame(oldUniversity, newUniversity);
        assertEquals(oldUniversity, manager.loadFromFile(file));
        Group restored = firstGroup(newUniversity);
        assertInstanceOf(Student.class, restored.getHead());
        assertEquals(restored.getStudents().get(0), restored.getHead());
        assertThrows(UnsupportedOperationException.class, () -> restored.getStudents().clear());
    }

    @ParameterizedTest
    @MethodSource("changes")
    void equalityDetectsChangesAtEveryLevel(Consumer<University> change) {
        University original = Run.createTypycalUniversity();
        University changed = manager.fromJson(manager.toJson(original));
        change.accept(changed);
        assertNotEquals(original, changed);
        assertNotEquals(changed, original);
    }

    static Stream<Consumer<University>> changes() {
        return Stream.of(
                u -> u.setName("Інший університет"),
                u -> u.getHead().setFirstName("Інший ректор"),
                u -> u.getFaculties().get(0).setName("Інший факультет"),
                u -> u.getFaculties().get(0).getHead().setLastName("Інший декан"),
                u -> u.getFaculties().get(0).getDepartments().get(0).setName("Інша кафедра"),
                u -> u.getFaculties().get(0).getDepartments().get(0).getHead().setSex(Sex.MALE),
                u -> firstGroup(u).setName("Інша група"),
                u -> firstGroup(u).getHead().setLastName("Інший керівник"),
                u -> firstGroup(u).getStudents().get(1).setPatronymic("Інше по батькові"),
                u -> firstGroup(u).getStudents().get(0).setSex(Sex.FEMALE),
                u -> firstGroup(u).addStudent(new Student("А", "Б", "В", Sex.MALE)));
    }

    @Test
    void equalityContractsHoldForEveryModelType() {
        University first = Run.createTypycalUniversity();
        University second = Run.createTypycalUniversity();
        University third = manager.fromJson(manager.toJson(first));
        Object[][] triples = {
            { first, second, third },
            { first.getFaculties().get(0), second.getFaculties().get(0), third.getFaculties().get(0) },
            { first.getFaculties().get(0).getDepartments().get(0),
                second.getFaculties().get(0).getDepartments().get(0),
                third.getFaculties().get(0).getDepartments().get(0) },
            { firstGroup(first), firstGroup(second), firstGroup(third) },
            { first.getHead(), second.getHead(), third.getHead() },
            { firstGroup(first).getStudents().get(0), firstGroup(second).getStudents().get(0),
                firstGroup(third).getStudents().get(0) },
            { Sex.MALE, Sex.MALE, Sex.MALE }
        };
        for (Object[] triple : triples) {
            assertEquals(triple[0], triple[0]);
            assertEquals(triple[0], triple[1]);
            assertEquals(triple[1], triple[0]);
            assertEquals(triple[1], triple[2]);
            assertEquals(triple[0], triple[2]);
            assertEquals(triple[0].hashCode(), triple[1].hashCode());
            assertFalse(triple[0].equals(null));
            assertNotEquals(triple[0], "university");
        }
        Human human = new Human("А", "Б", "В", Sex.MALE);
        Student student = new Student("А", "Б", "В", Sex.MALE);
        assertNotEquals(human, student);
        assertNotEquals(student, human);
        assertNotEquals(new University("А", human, Collections.emptyList()),
                new Faculty("А", human, Collections.emptyList()));
        ua.example.lab3.model.Human legacyHead = new ua.example.lab3.model.Human(
                "А", "Б", "В", ua.example.lab3.model.Gender.MALE);
        ua.example.lab3.model.University legacyUniversity = new ua.example.lab3.model.University(
                "А", legacyHead, Collections.emptyList());
        ua.example.lab3.model.Faculty legacyFaculty = new ua.example.lab3.model.Faculty(
                "А", legacyHead, Collections.emptyList());
        assertNotEquals(legacyUniversity, legacyFaculty);
        assertNotEquals(legacyFaculty, legacyUniversity);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "null", "{}", "[]", "{bad json}", "{\"name\":1}",
            "{\"name\":\"test\",\"faculties\":[]}", "{} trailing", "{'name':'test'}" })
    void invalidJsonIsRejected(String json) {
        assertThrows(JsonParseException.class, () -> manager.fromJson(json));
    }

    @Test
    void invalidNestedDataIsRejected() {
        JsonObject json = JsonParser.parseString(manager.toJson(Run.createTypycalUniversity())).getAsJsonObject();
        json.getAsJsonArray("faculties").get(0).getAsJsonObject().add("departments", null);
        final String invalidDepartments = json.toString();
        assertThrows(JsonParseException.class, () -> manager.fromJson(invalidDepartments));
        json = JsonParser.parseString(manager.toJson(Run.createTypycalUniversity())).getAsJsonObject();
        json.getAsJsonObject("head").addProperty("sex", "UNKNOWN");
        final String invalidSex = json.toString();
        assertThrows(JsonParseException.class, () -> manager.fromJson(invalidSex));
    }

    @Test
    void ioFailuresAreReported() {
        assertThrows(IOException.class, () -> manager.loadFromFile(temporaryDirectory.resolve("absent.json")));
        assertThrows(IOException.class, () -> manager.saveToFile(Run.createTypycalUniversity(), temporaryDirectory));
    }

    @Test
    void emptyHierarchyAndEscapedNamesRoundTrip() {
        University university = new University("Університет \"А\"\nКиїв \\ Україна",
                new Human("Ім'я", "Прізвище", "По батькові", Sex.FEMALE), Collections.emptyList());
        assertEquals(university, manager.fromJson(manager.toJson(university)));
    }

    private static Group firstGroup(University university) {
        return university.getFaculties().get(0).getDepartments().get(0).getGroups().get(0);
    }
}
