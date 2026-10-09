
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordTest {

    @Test
    void createsTemperatureRecord() {

        TemperatureUnit celsius =
                new TemperatureUnit(1, "Celsius", "°C");

        TemperatureUnit fahrenheit =
                new TemperatureUnit(2, "Fahrenheit", "°F");

        TempRecord record = new TempRecord(
                10,
                20,
                celsius,
                68,
                fahrenheit
        );

        assertEquals(10, record.getId());
        assertEquals(20, record.getInputValue());
        assertEquals(celsius, record.getOriginalUnit());
        assertEquals(68, record.getOutputValue());
        assertEquals(fahrenheit, record.getTargetUnit());
    }

    @Test
    void toStringShowsConversion() {

        TemperatureUnit celsius =
                new TemperatureUnit(1, "Celsius", "°C");

        TemperatureUnit fahrenheit =
                new TemperatureUnit(2, "Fahrenheit", "°F");

        TempRecord record = new TempRecord(
                1, 20, celsius, 68, fahrenheit
        );

        assertEquals(
                "20.00 °C → 68.00 °F",
                record.toString()
        );
    }
}
