
public class TempCalculator {

    private final TemperatureConverter converter;

    public TempCalculator() {
        this.converter = new TemperatureConverter();
    }

    public double convert(
            double temperature,
            TemperatureUnit originalUnit) {

        if (!Double.isFinite(temperature)) {
            throw new IllegalArgumentException(
                    "Temperature must be a finite number"
            );
        }

        return switch (originalUnit.getName()) {

            case "Celsius" ->
                    converter.celsiusToFahrenheit(temperature);

            case "Fahrenheit" ->
                    converter.fahrenheitToCelsius(temperature);

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported unit: " + originalUnit.getName()
                    );
        };
    }

    public boolean isExtremeTemperature(
            double temperature,
            TemperatureUnit originalUnit) {

        if (!Double.isFinite(temperature)) {
            throw new IllegalArgumentException(
                    "Temperature must be a finite number"
            );
        }

        double celsius;

        if (originalUnit.getName().equals("Fahrenheit")) {
            celsius = converter.fahrenheitToCelsius(temperature);

        } else if (originalUnit.getName().equals("Celsius")) {
            celsius = temperature;

        } else {
            throw new IllegalArgumentException(
                    "Unsupported unit: " + originalUnit.getName()
            );
        }

        return converter.isExtremeTemperature(celsius);
    }
}
