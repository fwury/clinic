import java.io.*;
import java.util.*;
import java.time.*;

/**
 * The {@code Clinic} class is responsible for storing patient data and
 * generating a report based on their vital records. It reads a structured input
 * file containing patient records, builds internal {@code Patient} objects, and
 * generates a report based on abnormal readings (alerts).
 * 
 * Input file format:
 * - PATIENT,<ID>,<Name>,<Sex (M, F, or X)>,<Date of Birth (YYYY-MM-DD)>
 * - RECORD,<ID>,<Date (YYYY-MM-DD)>,<Heart Rate>,<Systolic/Diastolic Blood
 * Pressure>,<Temperature (in celsius)>,<Respiratory Rate>
 */

public class Clinic {
    // Map of patients using ID as the key
    private Map<Long, Patient> patients;

    /**
     * Constructs a {@code Clinic} by reading and parsing the input file.
     * 
     * Precondition: The input file exists and follows the specified format.
     * Postcondition: Patient data and records are loaded into {@code patients}.
     * 
     * @param file the input file containing patient and record entries
     * @throws RuntimeException if file reading fails
     */
    public Clinic(File file) {
        patients = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // Split the line into components using a comma as the delimiter
                String[] parts = line.split(",");
                String type = parts[0];

                // Dispatch the line to the appropriate handler based on type
                if (type.equals("PATIENT")) {
                    handlePatient(parts, line);
                } else if (type.equals("RECORD")) {
                    handleRecord(parts, line);
                } else {
                    // Issue a warning if the line type is not recognized
                    System.out.println("[WARN] Invalid format, skipping entry: " + line);
                }
            }

            reader.close();
        } catch (IOException e) {
            // Throw RuntimeException if file reading fails
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Parses a PATIENT entry and adds it to {@code patients}.
     * 
     * Precondition: {@code parts} represents a correctly-formatted PATIENT entry.
     * Postcondition: The {@code Patient} object is added to {@code patients}, or a
     * warning is issued if it is invalid or a duplicate.
     * 
     * @param parts the components of the line as a {@code String} array
     * @param line  the original input line (used for error reporting)
     */
    private void handlePatient(String[] parts, String line) {
        try {
            // Parse fields: ID, name, sex, and date of birth
            long id = Long.parseLong(parts[1]);
            String name = parts[2];
            char sex = parts[3].charAt(0);
            LocalDate dob = LocalDate.parse(parts[4]);

            // If ID already exists, issue a warning and skip the patient
            if (patients.containsKey(id)) {
                System.out.println("[WARN] Duplicate patient ID " + id + ", skipping entry: " + line);
                return;
            }

            // Add new Patient object into patients
            patients.put(id, new Patient(id, name, sex, dob));
        } catch (Exception e) {
            // Issue a warning for any parsing errors
            System.out.println("[WARN] Invalid format, skipping entry: " + line);
        }
    }

    /**
     * Parses a RECORD entry and adds the {@code Record} object to the
     * corresponding {@code Patient} object.
     * 
     * Precondition: {@code parts} represents a correctly-formatted RECORD entry
     * and refer to an existing {@code Patient} object.
     * Postcondition: The {@code Record} object is added to the corresponding
     * {@code Patient} object, or a warning is issued if it is invalid.
     * 
     * @param parts the components of the line as a {@code String} array
     * @param line  the original input line (used for error reporting)
     */
    private void handleRecord(String[] parts, String line) {
        // Declare patient ID, date of measurement, and vital measurements
        long id;
        LocalDate date;
        int heartRate, systolicBp, diastolicBp, respiratoryRate;
        double temperature;

        try {
            // Parse patient ID and date of measurement
            id = Long.parseLong(parts[1]);
            date = LocalDate.parse(parts[2]);

            // Parse heart rate
            heartRate = Integer.parseInt(parts[3]);

            // Split blood pressure into systolic and diastolic
            String[] bloodPressure = parts[4].split("/");
            systolicBp = Integer.parseInt(bloodPressure[0]);
            diastolicBp = Integer.parseInt(bloodPressure[1]);

            // Parse temperature and respiratory rate
            temperature = Double.parseDouble(parts[5]);
            respiratoryRate = Integer.parseInt(parts[6]);
        } catch (Exception e) {
            // Issue a warning for any parsing errors
            System.out.println("[WARN] Invalid format, skipping entry: " + line);
            return;
        }

        // Retrive the corresponding Patient object
        Patient patient = patients.get(id);

        // Issue a warning if the patient does not exist
        if (patient == null) {
            System.out.println("[WARN] Unknown patient ID " + id + ", skipping entry: " + line);
            return;
        }

        // Create and add each Record object to the patient's list of records
        patient.addRecord(new HeartRateRecord(date, heartRate));
        patient.addRecord(new BloodPressureRecord(date, systolicBp, diastolicBp));
        patient.addRecord(new TemperatureRecord(date, temperature));
        patient.addRecord(new RespiratoryRateRecord(date, respiratoryRate));
    }

    /**
     * Generates a report for all patients, sorted by the number of abnormal
     * records in descending order.
     * 
     * Precondition: The patients and their records are initialized.
     * Postcondition: A formatted report is returned as a {@code String}.
     * 
     * @return the formatted report as a {@code String}
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=========================\n      CLINIC REPORT      \n=========================");

        // Sort patients by number of alerts in descending order
        List<Patient> sortedList = Clinic.sort(new ArrayList<Patient>(patients.values()));

        // Append each patient's report to the overall report
        sortedList.forEach(patient -> report.append("\n\n").append(patient.generateReport()));

        return report.toString();
    }

    /**
     * Recursively sorts a list of {@code Patient} objects by the number of abnormal
     * readings (alerts) in descending order, using merge sort.
     * 
     * Precondition: The list is not empty.
     * Postcondition: A new sorted list is returned.
     * 
     * @param list the list of {@code Patient} objects to sort
     * @return a new list sorted in descending order by alert count
     */
    private static List<Patient> sort(List<Patient> list) {
        // Base case: a list with 0 or 1 element is already sorted
        if (list.size() <= 1) {
            return list;
        }

        // Split the list into two halves
        int mid = list.size() / 2;

        // Recursively sort both halves
        List<Patient> left = Clinic.sort(list.subList(0, mid));
        List<Patient> right = Clinic.sort(list.subList(mid, list.size()));

        // Merge the sorted halves into a single sorted list
        return Clinic.merge(left, right);
    }

    /**
     * Merges two sorted sublists of {@code Patient} objects into a single list
     * sorted in descending order by alert count.
     * 
     * Precondition: Both sublists are already sorted.
     * Postcondition: A single sorted list is returned.
     * 
     * @param left  the first sorted sublist
     * @param right the second sorted sublist
     * @return the merged and sorted list
     */
    private static List<Patient> merge(List<Patient> left, List<Patient> right) {
        // If one of the sublists is empty, return the other
        if (left.isEmpty()) {
            return right;
        }
        if (right.isEmpty()) {
            return left;
        }

        // Initialize the result list to store merged patients
        List<Patient> result = new ArrayList<Patient>();

        // Compare the number of alerts in the first element of each sublist
        // The patient with more alerts is added first to maintain descending order
        Patient firstLeft = left.get(0);
        Patient firstRight = right.get(0);

        if (firstLeft.numAlerts() >= firstRight.numAlerts()) {
            result.add(firstLeft);

            // Recurse: merge the rest of the left sublist with the right sublist
            result.addAll(Clinic.merge(left.subList(1, left.size()), right));
        } else {
            result.add(firstRight);

            // Recurse: merge the left sublist with the rest of the right sublist
            result.addAll(Clinic.merge(left, right.subList(1, right.size())));
        }

        // Return the merged list
        return result;
    }
}
