
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TempCalculatorTest {

    private final TempCalculator calculator =
            new TempCalculator();

    private final TemperatureUnit celsius =
            new TemperatureUnit(1, "Celsius", "°C");

    private final TemperatureUnit fahrenheit =
            new TemperatureUnit(2, "Fahrenheit", "°F");

    @Test
    void convertsCelsiusToFahrenheit() {

        double result = calculator.convert(
                20, celsius
        );

        assertEquals(68, result, 0.0001);
    }

    @Test
    void convertsFahrenheitToCelsius() {

        double result = calculator.convert(
                68, fahrenheit
        );

        assertEquals(20, result, 0.0001);
    }

    @Test
    void detectsExtremeCelsiusTemperature() {

        assertTrue(
                calculator.isExtremeTemperature(60, celsius)
        );

        assertTrue(
                calculator.isExtremeTemperature(-50, celsius)
        );

        assertFalse(
                calculator.isExtremeTemperature(20, celsius)
        );
    }

    @Test
    void detectsExtremeFahrenheitTemperature() {

        assertTrue(
                calculator.isExtremeTemperature(140, fahrenheit)
        );

        assertFalse(
                calculator.isExtremeTemperature(68, fahrenheit)
        );
    }

    @Test
    void rejectsUnsupportedUnit() {

        TemperatureUnit kelvin =
                new TemperatureUnit(3, "Kelvin", "K");

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.convert(300, kelvin)
        );
    }

    @Test
    void rejectsInvalidNumber() {

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.convert(Double.NaN, celsius)
        );
    }

    @Test
    void testsExtremeTemperatureBoundaries() {

        assertFalse(
                calculator.isExtremeTemperature(-40, celsius)
        );

        assertFalse(
                calculator.isExtremeTemperature(50, celsius)
        );

        assertTrue(
                calculator.isExtremeTemperature(50.1, celsius)
        );
    }
}
