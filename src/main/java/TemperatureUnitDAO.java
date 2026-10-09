
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class TemperatureUnitDAO {

    private final DBConnection dbConnection;

    public TemperatureUnitDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<TemperatureUnit> findAll() throws SQLException {

        List<TemperatureUnit> units = new ArrayList<>();

        String sql = """
            SELECT id, name, symbol
            FROM TemperatureUnit
            ORDER BY id
            """;

        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {

                TemperatureUnit unit = new TemperatureUnit(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("symbol")
                );

                units.add(unit);
            }
        }

        return units;
    }
}
