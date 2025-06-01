import java.util.*;
import java.time.*;

/**
 * The {@code Patient} class represents a patient in the clinic system. It
 * stores personal demographic information (ID, name, sex, and date of birth), a
 * list of vital records, and any alerts that result from abnormal readings.
 * 
 * Alerts are generated when a newly added {@code Record} indicates an abnormal
 * reading, determined by logic in the {@code Record#getAlert(char, Period)}
 * method.
 */
public class Patient {
    private long id; // Patient ID
    private String name; // Full name of the patient
    private char sex; // Sex of the patient ('M', 'F', or 'X')
    private LocalDate dob; // Date of birth
    private List<Record> records = new ArrayList<Record>(); // List of all vital records for the patient
    private List<String> alerts = new ArrayList<String>(); // List of alerts generated from abnormal readings

    /**
     * Constructs a {@code Patient} object with demographic information.
     * 
     * Precondition: {@code name} is not null or empty. {@code sex} is 'M', 'F', or
     * 'X'. {@code dob} is not null and represents a valid date in the past.
     * Postcondition: A new {@code Patient} object is initialized with no records or
     * alerts.
     * 
     * @param id   the ID of the patient
     * @param name the full name of the patient
     * @param sex  the sex of the patient ('M', 'F', or 'X')
     * @param dob  the date of birth of the patient
     * @throws RuntimeException if {@code name} is null or empty, {@code sex} is
     *                          invalid, or {@code dob} is null or in the future.
     */
    public Patient(long id, String name, char sex, LocalDate dob) {
        // Throw RuntimeException if name is null or empty
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Name must not be null or empty");
        }

        // Throw RuntimeException if sex is invalid
        if (sex != 'M' && sex != 'F' && sex != 'X') {
            throw new RuntimeException("Sex must be 'M', 'F', or 'X'");
        }

        // Throw RuntimeException if date of birth is null or in the future
        if (dob == null || dob.isAfter(LocalDate.now())) {
            throw new RuntimeException("Date of birth must be a non-null date in the past");
        }

        this.id = id;
        this.name = name;
        this.sex = sex;
        this.dob = dob;
    }

    /**
     * Returns the ID of the patient.
     * 
     * Precondition: The {@code Patient} object has been initialized.
     * Postcondition: The ID of the patient is returned.
     * 
     * @return the ID of the patient
     */
    public long getId() {
        return id;
    }

    /**
     * Calculates the age of the patient as a {@code Period}.
     * 
     * Precondition: The date of birth is a valid date in the past.
     * Postcondition: The age of the patient is returned as a {@code Period}.
     * 
     * @return the age of the patient
     */
    public Period getAge() {
        return Period.between(dob, LocalDate.now());
    }

    /**
     * Returns the number of alerts the patient has accumulated.
     * 
     * Precondition: Alerts have been generated through {@code addRecord()}.
     * Postcondition: The number of alerts is returned.
     * 
     * @return the number of alerts
     */
    public int numAlerts() {
        return alerts.size();
    }

    /**
     * Adds a new vital record to {@code records} and checks for alerts.
     * 
     * Precondition: {@code record} is not null.
     * Postcondition: The record is added to {@code records} and any associated
     * alerts are stored in {@code alerts}.
     * 
     * @param record the vital record to add
     */
    public void addRecord(Record record) {
        // Add the record to the patient's records
        records.add(record);

        // Generate an alert string for the record
        String alert = record.getAlert(sex, getAge());

        // If the alert is not empty, store it in the patient's alerts
        if (!alert.isEmpty()) {
            alerts.add(alert);
        }
    }

    /**
     * Generates a report for the patient, including personal demographics and
     * alerts.
     * 
     * Precondition: Records have been added through {@code addRecord()}.
     * Postcondition: A formatted report is returned as a {@code String}.
     * 
     * @return the formatted report as a {@code String}
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();

        // Header containing personal demographics
        report.append("Patient: ").append(name).append(" (").append(id).append(")\n");
        report.append("Sex: ").append(sex).append("\n");
        report.append("Age: ").append(getAge().getYears()).append(" (").append(dob).append(")\n\n");

        // Section for alerts
        report.append("-- Alerts --\n");

        if (alerts.isEmpty()) {
            report.append("No alerts.\n");
        } else {
            // Recursively append alert messages to the report
            appendAlert(report, 0);
        }

        report.append("\n-------------------------");
        return report.toString();
    }

    /**
     * Recursively appends alert messages to the report.
     * 
     * Precondition: {@code report} is a valid {@code StringBuilder} instance.
     * {@code i < alerts.size()}.
     * Postcondition: Alert messages are appended to the {@code report} object.
     * 
     * @param report the StringBuilder to append alerts to
     * @param i      the current index of the alert to append
     */
    private void appendAlert(StringBuilder report, int i) {
        // Base case: if index exceeds alerts.size(), stop recursion
        if (i >= alerts.size()) {
            return;
        }

        // Append current alert and recurse to the next
        report.append(alerts.get(i)).append("\n");
        appendAlert(report, i + 1);
    }
}