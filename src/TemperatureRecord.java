import java.io.*;
import java.util.*;
import java.time.*;

public class TemperatureRecord extends Record {
    private double temperature;

    public TemperatureRecord(LocalDate date, double temperature) {
        super(date);
        this.temperature = temperature;
    }

    @Override
    public String getAlert(char sex, Period age) {
        if (temperature >= 39.4) {
            return super.getDate() + " [Temperature] High fever (" + temperature + " \u00B0" + "C)";
        }
        
        if (temperature >= 38) {
            return super.getDate() + " [Temperature] Moderate fever (" + temperature + " \u00B0" + "C)";
        }
        
        if (temperature < 35) {
            return super.getDate() + " [Temperature] Hypothermia (" + temperature + " \u00B0" + "C)";
        }
        
        return "";
    }
}
