
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class TempRecordDAO {

    private final DBConnection dbConnection;

    public TempRecordDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public int save(TempRecord record) throws SQLException {

        String sql = """
            INSERT INTO TempRecord
                (input_value, original_unit_id,
                 output_value, target_unit_id)
            VALUES (?, ?, ?, ?)
            """;

        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            statement.setDouble(
                    1, record.getInputValue()
            );

            statement.setInt(
                    2, record.getOriginalUnit().getId()
            );

            statement.setDouble(
                    3, record.getOutputValue()
            );

            statement.setInt(
                    4, record.getTargetUnit().getId()
            );

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException(
                    "Could not retrieve generated record ID"
            );
        }
    }

    public List<TempRecord> findAll() throws SQLException {

        List<TempRecord> records = new ArrayList<>();

        String sql = """
            SELECT
                r.id,
                r.input_value,
                r.output_value,

                original.id AS original_id,
                original.name AS original_name,
                original.symbol AS original_symbol,

                target.id AS target_id,
                target.name AS target_name,
                target.symbol AS target_symbol

            FROM TempRecord r

            JOIN TemperatureUnit original
                ON r.original_unit_id = original.id

            JOIN TemperatureUnit target
                ON r.target_unit_id = target.id

            ORDER BY r.id DESC
            """;

        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {

                TemperatureUnit originalUnit =
                        new TemperatureUnit(
                                resultSet.getInt("original_id"),
                                resultSet.getString("original_name"),
                                resultSet.getString("original_symbol")
                        );

                TemperatureUnit targetUnit =
                        new TemperatureUnit(
                                resultSet.getInt("target_id"),
                                resultSet.getString("target_name"),
                                resultSet.getString("target_symbol")
                        );

                TempRecord record = new TempRecord(
                        resultSet.getInt("id"),
                        resultSet.getDouble("input_value"),
                        originalUnit,
                        resultSet.getDouble("output_value"),
                        targetUnit
                );

                records.add(record);
            }
        }

        return records;
    }
}
