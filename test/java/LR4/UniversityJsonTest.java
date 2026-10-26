package LR4;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Stream;
import LR4.model.Department;
import LR4.model.Faculty;
import LR4.model.Group;
import LR4.model.Human;
import LR4.model.Sex;
import LR4.model.Student;
import LR4.model.University;
import LR4.persistence.JsonManager;
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
        ua.example.LR4.model.Human legacyHead = new ua.example.LR4.model.Human(
                "А", "Б", "В", ua.example.LR4.model.Gender.MALE);
        ua.example.LR4.model.University legacyUniversity = new ua.example.LR4.model.University(
                "А", legacyHead, Collections.emptyList());
        ua.example.LR4.model.Faculty legacyFaculty = new ua.example.LR4.model.Faculty(
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
    void overwritingJsonKeepsTheLatestDataAndLeavesNoTemporaryFiles() throws IOException {
        Path file = temporaryDirectory.resolve("університет.json");
        University university = Run.createTypycalUniversity();
        manager.saveToFile(university, file);
        university.setName("Оновлений університет");
        manager.saveToFile(university, file);
        assertEquals(university, manager.loadFromFile(file));
        assertThrows(NullPointerException.class, () -> manager.saveToFile(null, file));
        assertEquals(university, manager.loadFromFile(file));
        try (Stream<Path> files = Files.list(temporaryDirectory)) {
            assertEquals(1L, files.count());
        }
    }

    @Test
    void failedSaveDoesNotReplaceAnExistingDirectory() throws IOException {
        Path directory = Files.createDirectory(temporaryDirectory.resolve("directory.json"));
        Path existingFile = directory.resolve("keep.txt");
        Files.write(existingFile, "keep".getBytes(StandardCharsets.UTF_8));
        assertThrows(IOException.class, () -> manager.saveToFile(Run.createTypycalUniversity(), directory));
        assertEquals("keep", new String(Files.readAllBytes(existingFile), StandardCharsets.UTF_8));
        try (Stream<Path> files = Files.list(temporaryDirectory)) {
            assertEquals(1L, files.count());
        }
    }

    @ParameterizedTest
    @MethodSource("invalidFields")
    void invalidFieldsThroughoutHierarchyAreRejected(Consumer<JsonObject> invalidate) {
        JsonObject json = JsonParser.parseString(manager.toJson(Run.createTypycalUniversity())).getAsJsonObject();
        invalidate.accept(json);
        assertThrows(JsonParseException.class, () -> manager.fromJson(json.toString()));
    }

    static Stream<Consumer<JsonObject>> invalidFields() {
        return Stream.of(
                json -> json.add("head", null),
                json -> json.add("faculties", null),
                json -> { JsonArray faculties = new JsonArray(); faculties.add((JsonObject) null);
                    json.add("faculties", faculties); },
                json -> json.getAsJsonObject("head").addProperty("kind", "unknown"),
                json -> json.getAsJsonObject("head").remove("firstName"),
                json -> json.getAsJsonObject("head").add("sex", null),
                json -> json.getAsJsonArray("faculties").get(0).getAsJsonObject().remove("departments"),
                json -> firstGroupJson(json).add("students", null),
                json -> firstGroupJson(json).getAsJsonArray("students").get(0)
                        .getAsJsonObject().addProperty("kind", "human"));
    }

    private static JsonObject firstGroupJson(JsonObject json) {
        return json.getAsJsonArray("faculties").get(0).getAsJsonObject()
                .getAsJsonArray("departments").get(0).getAsJsonObject()
                .getAsJsonArray("groups").get(0).getAsJsonObject();
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
