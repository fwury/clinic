import java.io.*;
import java.util.*;
import java.time.*;

public class Patient {
    private int id;
    private String name;
    private char sex;
    private LocalDate dob;
    private List<Record> records = new ArrayList<Record>();
    private List<String> alerts = new ArrayList<String>();

    public Patient(int id, String name, char sex, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public Period getAge() {
        return Period.between(dob, LocalDate.now());
    }

    public int numAlerts() {
        return alerts.size();
    }

    public void addRecord(Record record) {
        records.add(record);
        
        String alert = record.getAlert(sex, getAge());
        
        if (!alert.isEmpty()) {
            alerts.add(alert);
        }
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Patient: ").append(name).append(" (").append(id).append(")\n");
        report.append("Sex: ").append(sex).append("\n");
        report.append("Age: ").append(getAge().getYears()).append(" (").append(dob).append(")\n\n");
        
        report.append("-- Alerts --\n");

        if (alerts.isEmpty()) {
            report.append("No alerts.\n");
        }

        else {
            appendAlert(report, 0);
        }
        
        report.append("\n-------------------------");

        return report.toString();
    }

    private void appendAlert(StringBuilder report, int i) {
        if (i >= alerts.size())
            return;
        
        report.append(alerts.get(i)).append("\n");
        appendAlert(report, i + 1);
    }
}
