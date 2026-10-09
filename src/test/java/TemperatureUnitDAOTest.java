
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TemperatureUnitDAOTest {

    @Test
    void findAllReturnsTemperatureUnits() throws Exception {

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

        when(resultSet.next())
                .thenReturn(true, true, false);

        when(resultSet.getInt("id"))
                .thenReturn(1, 2);

        when(resultSet.getString("name"))
                .thenReturn("Celsius", "Fahrenheit");

        when(resultSet.getString("symbol"))
                .thenReturn("°C", "°F");

        TemperatureUnitDAO dao =
                new TemperatureUnitDAO(db);

        List<TemperatureUnit> units = dao.findAll();

        assertEquals(2, units.size());
        assertEquals("Celsius", units.get(0).getName());
        assertEquals("Fahrenheit", units.get(1).getName());

        verify(connection).close();
        verify(statement).close();
        verify(resultSet).close();
    }
}
