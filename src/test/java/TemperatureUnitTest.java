
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitTest {

    @Test
    void createsTemperatureUnit() {

        TemperatureUnit unit =
                new TemperatureUnit(1, "Celsius", "°C");

        assertEquals(1, unit.getId());
        assertEquals("Celsius", unit.getName());
        assertEquals("°C", unit.getSymbol());
    }

    @Test
    void toStringReturnsName() {

        TemperatureUnit unit =
                new TemperatureUnit(2, "Fahrenheit", "°F");

        assertEquals("Fahrenheit", unit.toString());
    }
}
