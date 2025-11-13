package lotto.infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static final String USER = "lightplu";
    private static final String PASSWORD = "1234";

    private DBConnectionManager() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}