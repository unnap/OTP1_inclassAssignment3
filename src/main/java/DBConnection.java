
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final String url;
    private final String username;
    private final String password;

    public DBConnection() {
        this(
                System.getenv().getOrDefault(
                        "DB_URL",
                        "jdbc:mariadb://localhost:3306/temperature_db"
                ),
                System.getenv().getOrDefault(
                        "DB_USER",
                        "tempuser"
                ),
                System.getenv().getOrDefault(
                        "DB_PASSWORD",
                        "password"
                )
        );
    }

    public DBConnection(
            String url,
            String username,
            String password) {

        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                url, username, password
        );
    }
}
