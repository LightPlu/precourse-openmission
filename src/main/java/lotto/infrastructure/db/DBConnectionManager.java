package lotto.infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

    private static String url = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static String user = "lightplu";
    private static String password = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void override(String newUrl, String newUsername, String newPassword) {
        url = newUrl;
        user = newUsername;
        password = newPassword;
    }
}