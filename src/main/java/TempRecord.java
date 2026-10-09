
import java.util.Locale;

public class TempRecord {

    private final int id;
    private final double inputValue;
    private final TemperatureUnit originalUnit;
    private final double outputValue;
    private final TemperatureUnit targetUnit;

    public TempRecord(
            int id,
            double inputValue,
            TemperatureUnit originalUnit,
            double outputValue,
            TemperatureUnit targetUnit) {

        this.id = id;
        this.inputValue = inputValue;
        this.originalUnit = originalUnit;
        this.outputValue = outputValue;
        this.targetUnit = targetUnit;
    }

    public int getId() {
        return id;
    }

    public double getInputValue() {
        return inputValue;
    }

    public TemperatureUnit getOriginalUnit() {
        return originalUnit;
    }

    public double getOutputValue() {
        return outputValue;
    }

    public TemperatureUnit getTargetUnit() {
        return targetUnit;
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US,
                "%.2f %s → %.2f %s",
                inputValue,
                originalUnit.getSymbol(),
                outputValue,
                targetUnit.getSymbol()
        );
    }
}
