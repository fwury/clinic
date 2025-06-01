import java.io.*;
import java.util.*;
import java.time.*;

public class Record {
    private LocalDate date;
    
    public Record(LocalDate date) {
        this.date = date;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public String getAlert(char sex, Period age) {
        return "";
    }
}
