package lab3.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.Strictness;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import lab3.model.Department;
import lab3.model.Faculty;
import lab3.model.Group;
import lab3.model.Human;
import lab3.model.NamedUnit;
import lab3.model.Sex;
import lab3.model.Student;
import lab3.model.University;

/** JSON persistence, separate from the model and Swing view. */
public final class JsonManager {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setStrictness(Strictness.STRICT)
            .registerTypeHierarchyAdapter(Human.class, new HumanAdapter())
            .registerTypeHierarchyAdapter(NamedUnit.class, new UnitAdapter())
            .create();

    public String toJson(University university) {
        return gson.toJson(Objects.requireNonNull(university, "university"));
    }

    public University fromJson(String json) {
        Objects.requireNonNull(json, "json");
        try {
            University university = gson.fromJson(json, University.class);
            if (university == null) {
                throw new JsonParseException("JSON не містить університету.");
            }
            return university;
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException exception) {
            throw new JsonParseException("Некоректні дані університету.", exception);
        }
    }

    public void saveToFile(University university, Path file) throws IOException {
        byte[] json = toJson(university).getBytes(StandardCharsets.UTF_8);
        Path destination = Objects.requireNonNull(file, "file").toAbsolutePath();
        if (Files.isDirectory(destination)) {
            throw new IOException("Шлях призначення є каталогом: " + destination);
        }
        Path temporary = Files.createTempFile(destination.getParent(), ".university-", ".tmp");
        try {
            Files.write(temporary, json);
            try {
                Files.move(temporary, destination,
                        StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    public University loadFromFile(Path file) throws IOException {
        return fromJson(new String(Files.readAllBytes(file), StandardCharsets.UTF_8));
    }

    /** A discriminator preserves Student values stored in Human-typed head fields. */
    private static final class HumanAdapter
            implements JsonSerializer<Human>, JsonDeserializer<Human> {
        @Override
        public JsonElement serialize(Human human, Type type, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("kind", human instanceof Student ? "student" : "human");
            json.addProperty("firstName", human.getFirstName());
            json.addProperty("lastName", human.getLastName());
            json.addProperty("patronymic", human.getPatronymic());
            json.addProperty("sex", human.getSex().name());
            return json;
        }

        @Override
        public Human deserialize(JsonElement element, Type type, JsonDeserializationContext context) {
            JsonObject json = element.getAsJsonObject();
            String kind = requiredString(json, "kind");
            String first = requiredString(json, "firstName");
            String last = requiredString(json, "lastName");
            String patronymic = requiredString(json, "patronymic");
            Sex sex = Sex.valueOf(requiredString(json, "sex"));
            if ("student".equals(kind)) {
                return new Student(first, last, patronymic, sex);
            }
            if (!"human".equals(kind) || type == Student.class) {
                throw new JsonParseException("Невідомий або невідповідний тип людини: " + kind);
            }
            return new Human(first, last, patronymic, sex);
        }
    }

    /** Use existing constructors so missing fields and null list entries are rejected. */
    private static final class UnitAdapter implements JsonDeserializer<NamedUnit> {
        @Override
        public NamedUnit deserialize(JsonElement element, Type type, JsonDeserializationContext context) {
            JsonObject json = element.getAsJsonObject();
            String name = requiredString(json, "name");
            Human head = context.deserialize(json.get("head"), Human.class);
            if (type == University.class) {
                return new University(name, head, list(json, "faculties", Faculty.class, context));
            }
            if (type == Faculty.class) {
                return new Faculty(name, head, list(json, "departments", Department.class, context));
            }
            if (type == Department.class) {
                return new Department(name, head, list(json, "groups", Group.class, context));
            }
            if (type == Group.class) {
                return new Group(name, head, list(json, "students", Student.class, context));
            }
            throw new JsonParseException("Невідомий тип підрозділу: " + type);
        }

        private <T> List<T> list(JsonObject json, String field, Class<T> itemType,
                JsonDeserializationContext context) {
            return context.deserialize(json.get(field),
                    TypeToken.getParameterized(List.class, itemType).getType());
        }
    }

    private static String requiredString(JsonObject json, String field) {
        JsonElement value = json.get(field);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Відсутнє або некоректне текстове поле: " + field);
        }
        return value.getAsString();
    }
}
