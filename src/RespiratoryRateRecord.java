import java.io.*;
import java.util.*;
import java.time.*;

public class RespiratoryRateRecord extends Record {
    private int respiratoryRate;

    public RespiratoryRateRecord(LocalDate date, int respiratoryRate) {
        super(date);
        this.respiratoryRate = respiratoryRate;
    }

    @Override
    public String getAlert(char sex, Period age) {
        int lower;
        int upper;
        int ageYears = age.getYears();
        
        if (ageYears < 1) {
            lower = 30;
            upper = 60;
        }
        else if (ageYears < 3) {
            lower = 24;
            upper = 40;
        }
        else if (ageYears < 6) {
            lower = 20;
            upper = 34;
        }
        else if (ageYears < 13) {
            lower = 15;
            upper = 30;
        }
        else {
            lower = 12;
            upper = 20;
        }
        
        if (respiratoryRate > upper) {
            return super.getDate() + " [Heart Rate] Possible tachypnea (" + respiratoryRate + ")";
        }
        
        if (respiratoryRate < lower) {
            return super.getDate() + " [Heart Rate] Possible bradypnea (" + respiratoryRate + ")";
        }
        
        return "";
    }
}
