import java.time.*;

/**
 * The {@code HeartRateRecord} class represents a single heart rate measurement
 * taken on a specific date. It extends the {@code Record} class and includes
 * logic to determine whether the heart rate falls outside of the normal ranges.
 */
public class HeartRateRecord extends Record {
    // Heart rate in beats per minute (bpm)
    private int heartRate;

    /**
     * Constructs a {@code HeartRateRecord} object with the specified date and heart
     * rate value.
     * 
     * Precondition: {@code date} is not null.
     * Postcondition: A new {@code HeartRateRecord} object is initialized with the
     * given date and value.
     * 
     * @param date      the date the heart rate was recorded
     * @param heartRate the heart rate in beats per minute (bpm)
     */
    public HeartRateRecord(LocalDate date, int heartRate) {
        super(date);
        this.heartRate = heartRate;
    }

    /**
     * Returns an alert message if the heart rate is outside of the normal range for
     * the patient's age. Normal values are referenced from UIowa protocols
     * (https://medicine.uiowa.edu/iowaprotocols/pediatric-vital-signs-normal-ranges).
     * 
     * Precondition: {@code age} is not null.
     * Postcondition: An alert is returned if the heart rate is outside normal
     * bounds; otherwise, an empty string is returned.
     * 
     * @param sex the sex of the patient ('M', 'F', or 'X'); not used in this
     *            implementation
     * @param age the age of the patient as a {@code Period}
     * @return an alert if heart rate is abnormal; otherwise an empty string
     */
    @Override
    public String getAlert(char sex, Period age) {
        // Convert age to years
        int ageYears = age.getYears();

        // Define normal heart rate bounds based on age
        int lower;
        int upper;

        if (ageYears < 1) {
            lower = 100;
            upper = 170;
        } else if (ageYears < 3) {
            lower = 80;
            upper = 150;
        } else if (ageYears < 6) {
            lower = 70;
            upper = 130;
        } else if (ageYears < 13) {
            lower = 65;
            upper = 120;
        } else {
            lower = 60;
            upper = 100;
        }

        // Check for elevated heart rate (tachycardia)
        if (heartRate > upper) {
            return getDate() + " [Heart Rate] Tachycardia (" + heartRate + " bpm)";
        }

        // Check for reduced heart rate (bradycardia)
        if (heartRate < lower) {
            return getDate() + " [Heart Rate] Bradycardia (" + heartRate + " bpm)";
        }

        // Heart rate is within the normal range
        return "";
    }
}
