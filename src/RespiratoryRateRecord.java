import java.time.*;

/**
 * The {@code RespiratoryRateRecord} class represents a single respiratory rate
 * measurement taken on a specific date. It extends the {@code Record} class and
 * includes logic to determine whether the respiratory rate falls outside of the
 * normal ranges.
 */
public class RespiratoryRateRecord extends Record {
    // Respiratory rate in breaths per minute
    private int respiratoryRate;

    /**
     * Constructs a {@code RespiratoryRateRecord} object with the specified date and
     * respiratory rate value.
     * 
     * Precondition: {@code date} is not null.
     * Postcondition: A new {@code RespiratoryRateRecord} object is initialized with
     * the
     * given date and value.
     * 
     * @param date            the date the respiratory rate was recorded
     * @param respiratoryRate respiratory rate in beats per minute (bpm)
     */
    public RespiratoryRateRecord(LocalDate date, int respiratoryRate) {
        super(date);
        this.respiratoryRate = respiratoryRate;
    }

    /**
     * Returns an alert message if the respiratory rate is outside of the normal
     * range. Normal values are referenced from UIowa protocols
     * (https://medicine.uiowa.edu/iowaprotocols/pediatric-vital-signs-normal-ranges).
     * 
     * Precondition: {@code age} is not null.
     * Postcondition: An alert is returned if the respiratory rate is outside normal
     * bounds; otherwise, an empty string is returned.
     * 
     * @param sex the sex of the patient ('M', 'F', or 'X'); not used in this
     *            implementation
     * @param age the age of the patient as a {@code Period}
     * @return an alert if temperature is abnormal; otherwise an empty string
     */
    @Override
    public String getAlert(char sex, Period age) {
        // Convert age to years
        int ageYears = age.getYears();

        // Define normal respiratory rate bounds based on age
        int lower;
        int upper;

        if (ageYears < 1) {
            lower = 30;
            upper = 60;
        } else if (ageYears < 3) {
            lower = 24;
            upper = 40;
        } else if (ageYears < 6) {
            lower = 20;
            upper = 34;
        } else if (ageYears < 13) {
            lower = 15;
            upper = 30;
        } else {
            lower = 12;
            upper = 20;
        }

        // Check for tachypnea
        if (respiratoryRate > upper) {
            return getDate() + " [Respiratory Rate] Tachypnea (" + respiratoryRate + " breaths/min)";
        }

        // Check for bradypnea
        if (respiratoryRate < lower) {
            return getDate() + " [Respiratory Rate] Bradypnea (" + respiratoryRate + " breaths/min)";
        }

        // Respiratory rate is within the normal range
        return "";
    }
}
