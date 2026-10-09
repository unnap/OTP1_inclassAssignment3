
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TempRecordDAOTest {

    @Test
    void saveInsertsRecord() throws Exception {

        DBConnection db = mock(DBConnection.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement =
                mock(PreparedStatement.class);
        ResultSet generatedKeys = mock(ResultSet.class);

        when(db.getConnection()).thenReturn(connection);

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)
        )).thenReturn(statement);

        when(statement.getGeneratedKeys())
                .thenReturn(generatedKeys);

        when(generatedKeys.next())
                .thenReturn(true);

        when(generatedKeys.getInt(1))
                .thenReturn(25);

        TemperatureUnit celsius =
                new TemperatureUnit(1, "Celsius", "°C");

        TemperatureUnit fahrenheit =
                new TemperatureUnit(2, "Fahrenheit", "°F");

        TempRecord record = new TempRecord(
                0,
                20,
                celsius,
                68,
                fahrenheit
        );

        TempRecordDAO dao = new TempRecordDAO(db);

        int generatedId = dao.save(record);

        assertEquals(25, generatedId);

        verify(statement).setDouble(1, 20);
        verify(statement).setInt(2, 1);
        verify(statement).setDouble(3, 68);
        verify(statement).setInt(4, 2);
        verify(statement).executeUpdate();
    }

    @Test
    void findAllReturnsHistory() throws Exception {

        DBConnection db = mock(DBConnection.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement =
                mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(db.getConnection()).thenReturn(connection);

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getInt("id")).thenReturn(1);

        when(resultSet.getDouble("input_value"))
                .thenReturn(20.0);

        when(resultSet.getDouble("output_value"))
                .thenReturn(68.0);

        when(resultSet.getInt("original_id"))
                .thenReturn(1);

        when(resultSet.getString("original_name"))
                .thenReturn("Celsius");

        when(resultSet.getString("original_symbol"))
                .thenReturn("°C");

        when(resultSet.getInt("target_id"))
                .thenReturn(2);

        when(resultSet.getString("target_name"))
                .thenReturn("Fahrenheit");

        when(resultSet.getString("target_symbol"))
                .thenReturn("°F");

        TempRecordDAO dao = new TempRecordDAO(db);

        List<TempRecord> history = dao.findAll();

        assertEquals(1, history.size());

        assertEquals(
                "20.00 °C → 68.00 °F",
                history.get(0).toString()
        );

        verify(connection).close();
        verify(statement).close();
        verify(resultSet).close();
    }
}
