import java.io.*;
import java.util.*;
import java.time.*;

public class Clinic {
    private Map<Integer, Patient> patients;
    
    public Clinic(File file) {
        patients = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("//"))
                    continue;
                
                String[] parts = line.split(",");
                String type = parts[0];
                
                if (type.equals("PATIENT"))
                    handlePatient(parts, line);
                else if (type.equals("VITALS"))
                    handleVitals(parts, line);
                else
                    throw new IllegalArgumentException("Unrecognized format: " + line);
            }
        }
        
        catch (Exception e) {
            throw new RuntimeException("Error reading from input file.");
        }
    }
    
    private void handlePatient(String[] parts, String line) {
        try {
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            char sex = parts[3].charAt(0);
            LocalDate dob = LocalDate.parse(parts[4]);
            
            patients.put(id, new Patient(id, name, sex, dob));
        }
        
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid format for patient registration: " + line);
        }
    }

    private void handleVitals(String[] parts, String line) {
        int id;
        LocalDate date;
        int heartRate;
        int systolicBp;
        int diastolicBp;
        double temperature;
        int respiratoryRate;

        try {
            id = Integer.parseInt(parts[1]);
            date = LocalDate.parse(parts[2]);
            heartRate = Integer.parseInt(parts[3]);
            String[] bloodPressure = parts[4].split("/");
            systolicBp = Integer.parseInt(bloodPressure[0]);
            diastolicBp = Integer.parseInt(bloodPressure[1]);
            temperature = Double.parseDouble(parts[5]);
            respiratoryRate = Integer.parseInt(parts[6]);
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Invalid format for vitals: " + line);
        }

        Patient patient = patients.get(id);

        if (patient != null) {
            Record heartRateRecord = new HeartRateRecord(date, heartRate);
            Record bpRecord = new BloodPressureRecord(date, systolicBp, diastolicBp);
            Record temperatureRecord = new TemperatureRecord(date, temperature);
            Record respiratoryRateRecord = new RespiratoryRateRecord(date, respiratoryRate);
            
            patient.addRecord(heartRateRecord);
            patient.addRecord(bpRecord);
            patient.addRecord(temperatureRecord);
            patient.addRecord(respiratoryRateRecord);
        }
        
        else
            throw new IllegalArgumentException("Patient " + id + " does not exist: " + line);
    }
    
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=========================\nCLINIC REPORT\n=========================");
        
        List<Patient> sortedList = sort(new ArrayList<Patient>(patients.values()));
        
        sortedList.forEach(patient -> report.append("\n\n").append(patient.generateReport()));
        
        return report.toString();
    }
    
    private List<Patient> sort(List<Patient> list) {
        if (list.size() <= 1)
            return list;
        
        int mid = list.size() / 2;

        List<Patient> left = sort(list.subList(0, mid));
        List<Patient> right = sort(list.subList(mid, list.size()));

        return merge(left, right);
    }
    
    private List<Patient> merge(List<Patient> left, List<Patient> right) {
        if (left.isEmpty())
            return right;
        
        if (right.isEmpty())
            return left;

        Patient firstLeft = left.get(0);
        Patient firstRight = right.get(0);

        List<Patient> result = new ArrayList<Patient>();

        if (firstLeft.numAlerts() >= firstRight.numAlerts()) {
            result.add(firstLeft);
            result.addAll(merge(left.subList(1, left.size()), right));
        }

        else {
            result.add(firstRight);
            result.addAll(merge(left, right.subList(1, right.size())));
        }

        return result;
    }
}
