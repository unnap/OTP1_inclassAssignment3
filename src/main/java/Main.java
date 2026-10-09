
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class Main extends Application {

    private final TempCalculator calculator =
            new TempCalculator();

    private TemperatureUnitDAO unitDAO;
    private TempRecordDAO recordDAO;

    private TextField temperatureField;
    private ComboBox<TemperatureUnit> unitBox;
    private Label resultLabel;
    private ListView<TempRecord> historyList;

    private List<TemperatureUnit> availableUnits;

    @Override
    public void start(Stage stage) {

        DBConnection dbConnection = new DBConnection();

        unitDAO = new TemperatureUnitDAO(dbConnection);
        recordDAO = new TempRecordDAO(dbConnection);

        Label titleLabel = new Label(
                "Temperature Converter"
        );

        temperatureField = new TextField();
        temperatureField.setPromptText(
                "Enter temperature"
        );

        unitBox = new ComboBox<>();
        unitBox.setPromptText(
                "Choose temperature unit"
        );

        Button convertButton = new Button("Convert");

        resultLabel = new Label("Result:");
        resultLabel.setTextFill(Color.BLACK);

        Label historyLabel = new Label(
                "Conversion History"
        );

        historyList = new ListView<>();

        convertButton.setOnAction(event -> {
            convertTemperature();
        });

        VBox root = new VBox(12);

        root.setPadding(new Insets(20));

        root.getChildren().addAll(
                titleLabel,
                temperatureField,
                unitBox,
                convertButton,
                resultLabel,
                historyLabel,
                historyList
        );

        VBox.setVgrow(
                historyList,
                Priority.ALWAYS
        );

        Scene scene = new Scene(root, 450, 500);

        stage.setTitle("Temperature Converter");
        stage.setScene(scene);
        stage.show();

        loadUnits();
        loadHistory();
    }

    private void loadUnits() {

        try {
            // Retrieve units from MariaDB
            availableUnits = unitDAO.findAll();

            // Only show Celsius and Fahrenheit.
            // Kelvin remains in the database.
            List<TemperatureUnit> selectableUnits =
                    availableUnits.stream()
                            .filter(unit ->
                                    unit.getName().equals("Celsius")
                                            || unit.getName().equals("Fahrenheit")
                            )
                            .toList();

            unitBox.getItems().setAll(selectableUnits);

            if (!selectableUnits.isEmpty()) {
                unitBox.getSelectionModel().selectFirst();
            }

        } catch (SQLException exception) {
            showError(
                    "Unable to load temperature units:\n"
                            + exception.getMessage()
            );
        }
    }

    private TemperatureUnit findTargetUnit(
            TemperatureUnit originalUnit) {

        String targetName;

        if (originalUnit.getName().equals("Celsius")) {
            targetName = "Fahrenheit";
        } else {
            targetName = "Celsius";
        }

        return availableUnits.stream()
                .filter(unit ->
                        unit.getName().equals(targetName)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Target temperature unit not found"
                        )
                );
    }

    private void convertTemperature() {

        try {
            String inputText =
                    temperatureField.getText().trim();

            if (inputText.isEmpty()) {
                showError("Please enter a temperature.");
                return;
            }

            TemperatureUnit originalUnit =
                    unitBox.getValue();

            if (originalUnit == null) {
                showError(
                        "Please choose a temperature unit."
                );
                return;
            }

            double inputValue =
                    Double.parseDouble(inputText);

            TemperatureUnit targetUnit =
                    findTargetUnit(originalUnit);

            // Convert using TempCalculator
            double outputValue = calculator.convert(
                    inputValue,
                    originalUnit
            );

            // Check extreme temperature
            boolean isExtreme =
                    calculator.isExtremeTemperature(
                            inputValue,
                            originalUnit
                    );

            // Create a record for the conversion.
            // ID 0 means MariaDB will generate the ID.
            TempRecord record = new TempRecord(
                    0,
                    inputValue,
                    originalUnit,
                    outputValue,
                    targetUnit
            );

            // Save the conversion to MariaDB
            recordDAO.save(record);

            // Display the converted temperature
            resultLabel.setText(
                    formatResult(record)
            );

            if (isExtreme) {
                resultLabel.setTextFill(Color.RED);
            } else {
                resultLabel.setTextFill(Color.BLACK);
            }

            // Refresh history from MariaDB
            loadHistory();

        } catch (NumberFormatException exception) {
            showError(
                    "Please enter a valid numeric temperature."
            );

        } catch (SQLException exception) {
            showError(
                    "Database error:\n"
                            + exception.getMessage()
            );

        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());

        } catch (IllegalStateException exception) {
            showError(exception.getMessage());
        }
    }

    public static String formatResult(TempRecord record) {

        return String.format(
                Locale.US,
                "Result: %.2f %s = %.2f %s",
                record.getInputValue(),
                record.getOriginalUnit().getSymbol(),
                record.getOutputValue(),
                record.getTargetUnit().getSymbol()
        );
    }

    private void loadHistory() {

        try {
            List<TempRecord> records =
                    recordDAO.findAll();

            historyList.getItems().setAll(records);

        } catch (SQLException exception) {
            showError(
                    "Unable to load history:\n"
                            + exception.getMessage()
            );
        }
    }

    private void showError(String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle("Temperature Converter");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
