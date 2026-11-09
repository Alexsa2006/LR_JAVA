package ua.example.LR5.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Loads LR5 connection settings from the process environment. */
public final class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DEFAULT_USER = "postgres";

    private DatabaseConnection() { }

    public static String getUrl() {
        return environmentValue("LR5_DB_URL", DEFAULT_URL);
    }

    public static String getUser() {
        return environmentValue("LR5_DB_USER", DEFAULT_USER);
    }

    public static Connection getConnection() throws SQLException {
        String password = System.getenv("LR5_DB_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalStateException(
                   );
        }
        return DriverManager.getConnection(getUrl(), getUser(), password);
    }

    private static String environmentValue(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
