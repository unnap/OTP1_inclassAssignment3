
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void defaultConstructorCreatesObject() {

        DBConnection db = new DBConnection();

        assertNotNull(db);
    }

    @Test
    void invalidJdbcUrlThrowsSQLException() {

        DBConnection db = new DBConnection(
                "jdbc:invalid://localhost/test",
                "user",
                "password"
        );

        assertThrows(
                SQLException.class,
                db::getConnection
        );
    }
}
