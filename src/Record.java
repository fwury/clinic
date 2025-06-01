import java.time.*;

/**
 * The {@code Record} class represents a generic medical record that stores the
 * date on which the record was taken. It serves as a superclass for specific
 * vital record types (such as heart rate or blood pressure).
 */
public class Record {
    // The date the record was taken
    private LocalDate date;

    /**
     * Constructs a {@code Record} object with the specified date.
     * 
     * Precondition: {@code date} is not null.
     * Postcondition: A new {@code Record} object is initialized with the given
     * date.
     * 
     * @param date the date the record was taken
     */
    public Record(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the date the record was taken.
     * 
     * Precondition: The record is initialized with a valid date.
     * Postcondition: The date associated with the record is returned.
     * 
     * @return the date the record was taken
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns an alert message based on the patient's sex and age. The base
     * implementation returns an empty string and is intended to be overridden by
     * subclasses that define specific alert logic.
     * 
     * Precondition: {@code sex} is 'M' or 'F'. {@code age} is not null.
     * Postcondition: An alert is returned if the record is abnormal; otherwise, an
     * empty string is returned.
     * 
     * @param sex the sex of the patient ('M', 'F', or 'X')
     * @param age the age of the patient as a {@code Period}
     * @return an alert if the record is abnormal; otherwise an empty string
     */
    public String getAlert(char sex, Period age) {
        return "";
    }
}