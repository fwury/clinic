import java.time.*;

/**
 * The {@code BloodPressureRecord} class represents a single blood pressure
 * measurement taken on a specific date. It extends the {@code Record} class and
 * includes logic to determine whether the blood pressure falls outside of the
 * normal ranges.
 */
public class BloodPressureRecord extends Record {
    // Systolic blood pressure in mmHg
    private int systolicBp;
    // Diastolic blood pressure in mmHg
    private int diastolicBp;

    /**
     * Constructs a {@code BloodPressureRecord} object with the specified date and
     * blood pressure values.
     * 
     * @param date        the date the blood pressure was recorded
     * @param systolicBp  the systolic blood pressure in mmHg
     * @param diastolicBp the diastolic blood pressure in mmHg
     */
    public BloodPressureRecord(LocalDate date, int systolicBp, int diastolicBp) {
        super(date);
        this.systolicBp = systolicBp;
        this.diastolicBp = diastolicBp;
    }

    /**
     * Returns an alert message if the blood pressure is outside of the normal range
     * for the patient's age and sex. Adult values are based on AHA hypertension
     * guidelines. Pediatric values are referenced from UIowa protocols
     * (https://medicine.uiowa.edu/iowaprotocols/pediatric-vital-signs-normal-ranges).
     * 
     * Precondition: {@code age} is not null. {@code sex} is 'M', 'F', or 'X'.
     * Postcondition: An alert is returned if the blood pressure is outside normal
     * bounds; otherwise, an empty string is returned.
     * 
     * @param sex the sex of the patient ('M', 'F', or 'X')
     * @param age the age of the patient as a {@code Period}
     * @return an alert if blood pressure is abnormal; otherwise an empty string
     */
    @Override
    public String getAlert(char sex, Period age) {
        int ageYears = age.getYears();

        // Adult evaluation: age 13 and above
        if (ageYears >= 13) {
            // Check for hypotension (<90/)
            if (systolicBp < 90) {
                return getDate() + " [Blood Pressure] Possible hypotension (" + systolicBp + "/" + diastolicBp
                        + " mmHg)";
            }

            // Check for hypertension stage 2 (≥140/ or /≥90)
            if (systolicBp >= 140 || diastolicBp >= 90) {
                return getDate() + " [Blood Pressure] Possible hypertension stage 2 (" + systolicBp + "/"
                        + diastolicBp + " mmHg)";
            }

            // Check for hypertension stage 1 (≥130/ or /≥80)
            if (systolicBp >= 130 || diastolicBp >= 80) {
                return getDate() + " [Blood Pressure] Possible hypertension stage 1 (" + systolicBp + "/"
                        + diastolicBp + " mmHg)";
            }

            // Check for elevated blood pressure (≥120 and <80)
            if (systolicBp >= 120) {
                return getDate() + " [Blood Pressure] Possible elevated blood pressure (" + systolicBp + "/"
                        + diastolicBp + " mmHg)";
            }

            // Blood pressure is within the normal range
            return "";
        }

        // Pediatric evaluation: age 12 and below
        // Calculate lower bound for systolic blood pressure based on age
        int hypotensionThreshold = Math.min(70 + 2 * ageYears, 90);

        // Calculate upper bound for blood pressure values based on sex and age
        int systolicHypertensionThreshold, diastolicHypertensionThreshold;

        if (sex == 'M') {
            systolicHypertensionThreshold = new int[] { 104, 101, 104, 105, 107, 110, 109, 111, 113, 115, 117, 119,
                    120 }[ageYears];
            diastolicHypertensionThreshold = new int[] { 56, 54, 58, 61, 64, 67, 73, 74, 75, 75, 76, 77, 78 }[ageYears];
        } else {
            systolicHypertensionThreshold = new int[] { 104, 102, 106, 107, 108, 110, 108, 110, 112, 114, 116, 118,
                    120 }[ageYears];
            diastolicHypertensionThreshold = new int[] { 56, 58, 62, 65, 67, 70, 73, 74, 75, 76, 77, 78, 78 }[ageYears];
        }

        // Check for pediatric hypotension
        if (systolicBp < hypotensionThreshold) {
            return getDate() + " [Blood Pressure] Possible hypotension (" + systolicBp + "/" + diastolicBp
                    + " mmHg)";
        }

        // Check for possible pediatric hypertension
        if (systolicBp >= systolicHypertensionThreshold || diastolicBp >= diastolicHypertensionThreshold) {
            return getDate() + " [Blood Pressure] Possible hypertension (" + systolicBp + "/" + diastolicBp
                    + " mmHg)";
        }

        // Blood pressure is within the normal pediatric range
        return "";
    }
}
