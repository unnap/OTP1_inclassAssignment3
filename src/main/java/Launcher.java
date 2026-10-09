
import javafx.application.Application;

import java.sql.SQLException;
import java.util.List;

public class Launcher {

    public static void main(String[] args)
            throws SQLException {

        if (args.length == 1
                && args[0].equals("--check-db")) {

            DBConnection db = new DBConnection();

            TemperatureUnitDAO dao =
                    new TemperatureUnitDAO(db);

            List<TemperatureUnit> units = dao.findAll();

            boolean hasCelsius = units.stream()
                    .anyMatch(unit ->
                            unit.getName().equals("Celsius")
                    );

            boolean hasFahrenheit = units.stream()
                    .anyMatch(unit ->
                            unit.getName().equals("Fahrenheit")
                    );

            boolean hasKelvin = units.stream()
                    .anyMatch(unit ->
                            unit.getName().equals("Kelvin")
                    );

            if (!hasCelsius || !hasFahrenheit || !hasKelvin) {
                throw new IllegalStateException(
                        "Required temperature units are missing"
                );
            }

            System.out.println(
                    "Database connection and units OK"
            );

        } else {
            Application.launch(Main.class, args);
        }
    }
}
