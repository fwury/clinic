import java.io.*;
import java.util.*;
import java.time.*;

public class BloodPressureRecord extends Record {
    private int systolicBp;
    private int diastolicBp;

    public BloodPressureRecord(LocalDate date, int systolicBp, int diastolicBp) {
        super(date);
        this.systolicBp = systolicBp;
        this.diastolicBp = diastolicBp;
    }

    @Override
    public String getAlert(char sex, Period age) {
        int ageYears = age.getYears();
        
        if (ageYears >= 13) {
            if (systolicBp < 90) {
                return super.getDate() + " [Blood Pressure] Possible hypotension (" + systolicBp + "/" + diastolicBp + " mmHg)";
            }
            
            if (systolicBp >= 140 || diastolicBp >= 90) {
                return super.getDate() + " [Blood Pressure] Possible hypertension stage 2 (" + systolicBp + "/" + diastolicBp + " mmHg)";
            }
            
            if (systolicBp >= 130 || diastolicBp >= 80) {
                return super.getDate() + " [Blood Pressure] Possible hypertension stage 1 (" + systolicBp + "/" + diastolicBp + " mmHg)";
            }
            
            if (systolicBp >= 120) {
                return super.getDate() + " [Blood Pressure] Possible elevated blood pressure (" + systolicBp + "/" + diastolicBp + " mmHg)";
            }
            
            return "";
        }
        
        // Data from https://medicine.uiowa.edu/iowaprotocols/pediatric-vital-signs-normal-ranges
        else {
            int hypotension = Math.min(70 + 2 * ageYears, 90);
            int systolicHypertension, diastolicHypertension;
            
            if (sex == 'M') {
                systolicHypertension = new int[]{104, 101, 104, 105, 107, 110, 109, 111, 113, 115, 117, 119, 120}[ageYears];
                diastolicHypertension = new int[]{56, 54, 58, 61, 64, 67, 73, 74, 75, 75, 76, 77, 78}[ageYears];
            }
            
            else {
                systolicHypertension = new int[]{104, 102, 106, 107, 108, 110, 108, 110, 112, 114, 116, 118, 120}[ageYears];
                diastolicHypertension = new int[]{56, 58, 62, 65, 67, 70, 73, 74, 75, 76, 77, 78, 78}[ageYears];
            }
            
            if (systolicBp < hypotension) {
                return super.getDate() + " [Blood Pressure] Possible hypotension (" + systolicBp + "/" + diastolicBp + " mmHg)";
            }
            
            if (systolicBp >= systolicHypertension || diastolicBp >= diastolicHypertension) {
                return super.getDate() + " [Blood Pressure] Possible hypertension (" + systolicBp + "/" + diastolicBp + " mmHg)";
            }
            
            return "";
        }
    }
}
