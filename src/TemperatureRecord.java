import java.time.*;

/**
 * The {@code TemperatureRecord} class represents a single body temperature
 * measurement taken on a specific date. It extends the {@code Record} class and
 * includes logic to determine whether the temperature falls outside of the
 * normal ranges.
 */
public class TemperatureRecord extends Record {
    // Body temperature in degrees celsius (°C)
    private double temperature;

    /**
     * Constructs a {@code TemperatureRecord} object with the specified date and
     * temperature value.
     * 
     * Precondition: {@code date} is not null.
     * Postcondition: A new {@code TemperatureRecord} object is initialized with the
     * given date and value.
     * 
     * @param date        the date the temperature was recorded
     * @param temperature body temperature in celsius (°C)
     */
    public TemperatureRecord(LocalDate date, double temperature) {
        super(date);
        this.temperature = temperature;
    }

    /**
     * Returns an alert message if the temperature is outside of the normal range.
     * Values are referenced from Cleveland Clinic
     * (https://my.clevelandclinic.org/health/symptoms/10880-fever).
     * 
     * Precondition: None.
     * Postcondition: An alert is returned if the temperature is outside normal
     * bounds; otherwise, an empty string is returned.
     * 
     * @param sex the sex of the patient ('M', 'F', or 'X'); not used in this
     *            implementation
     * @param age the age of the patient as a {@code Period}; not used in this
     *            implementation
     * @return an alert if temperature is abnormal; otherwise an empty string
     */
    @Override
    public String getAlert(char sex, Period age) {
        // Check for high fever (≥39.4 °C)
        if (temperature >= 39.4) {
            return getDate() + " [Temperature] High fever (" + temperature + " \u00B0" + "C)";
        }

        // Check for moderate fever (≥38.0 °C)
        if (temperature >= 38) {
            return getDate() + " [Temperature] Moderate fever (" + temperature + " \u00B0" + "C)";
        }

        // Check for hypothermia (<35.0 °C)
        if (temperature < 35) {
            return getDate() + " [Temperature] Hypothermia (" + temperature + " \u00B0" + "C)";
        }

        // Body temperature is within the normal range
        return "";
    }
}
