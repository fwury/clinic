import java.io.*;
import java.util.*;
import java.time.*;

public class HeartRateRecord extends Record {
    private int heartRate;

    public HeartRateRecord(LocalDate date, int heartRate) {
        super(date);
        this.heartRate = heartRate;
    }
    
    // Data from https://medicine.uiowa.edu/iowaprotocols/pediatric-vital-signs-normal-ranges
    @Override
    public String getAlert(char sex, Period age) {
        int lower;
        int upper;
        int ageYears = age.getYears();
        
        if (ageYears < 1) {
            lower = 100;
            upper = 170;
        }
        else if (ageYears < 3) {
            lower = 80;
            upper = 150;
        }
        else if (ageYears < 6) {
            lower = 70;
            upper = 130;
        }
        else if (ageYears < 13) {
            lower = 65;
            upper = 120;
        }
        else {
            lower = 60;
            upper = 100;
        }
        
        if (heartRate > upper) {
            return super.getDate() + " [Heart Rate] Possible tachycardia (" + heartRate + " bpm)";
        }
        
        if (heartRate < lower) {
            return super.getDate() + " [Heart Rate] Possible bradycardia (" + heartRate + " bpm)";
        }
        
        return "";
    }
}
