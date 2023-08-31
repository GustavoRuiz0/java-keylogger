package cyber.key;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Data {

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/keysbase";
        String username = "postgres";
        String password = "1";
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
