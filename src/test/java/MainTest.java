
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void formatsConversionResult() {

        TemperatureUnit celsius =
                new TemperatureUnit(1, "Celsius", "°C");

        TemperatureUnit fahrenheit =
                new TemperatureUnit(2, "Fahrenheit", "°F");

        TempRecord record = new TempRecord(
                1,
                20,
                celsius,
                68,
                fahrenheit
        );

        String result = Main.formatResult(record);

        assertEquals(
                "Result: 20.00 °C = 68.00 °F",
                result
        );
    }
}
