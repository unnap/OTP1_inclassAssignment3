import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {
    TemperatureConverter tc = new TemperatureConverter();

    @org.junit.jupiter.api.Test
    void fahrenheitToCelsius() {
        double fahrenheit = 68;
        double expected = 20;

        double result = tc.fahrenheitToCelsius(fahrenheit);
        assertEquals(expected,result,0.0001);
    }

    @org.junit.jupiter.api.Test
    void celsiusToFahrenheit() {
        double celsius = 20;
        double expected = 68;

        double result = tc.celsiusToFahrenheit(celsius);
        assertEquals(expected,result,0.0001);
    }

    @org.junit.jupiter.api.Test
    void kelvinToCelsius() {
        double kelvin = 300;
        double expected = 26.85;

        double result = tc.kelvinToCelsius(kelvin);
        assertEquals(expected,result,0.0001);
    }

    @org.junit.jupiter.api.Test
    void isExtremeTemperature() {
        double hot = 51;
        double cold = -41;
        double normal = 20;

        assertTrue(tc.isExtremeTemperature(hot));
        assertTrue(tc.isExtremeTemperature(cold));
        assertFalse(tc.isExtremeTemperature(normal));
    }
}