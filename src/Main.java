import java.io.*;
import java.util.*;
import java.time.*;

public class Main {
    public static void main(String[] args) {
        File inputFile = new File("../input.txt");
        
        System.out.println("\nReading and processing data…");
        Clinic clinic = new Clinic(inputFile);
        
        System.out.println("Writing report…");
        String report = clinic.generateReport();
        
        File outputFile = new File("../output.txt");
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.print(report);
            System.out.println("Report written successfully.");
        }
        catch (Exception e) {
            throw new RuntimeException("Error writing to output file: " + e.getMessage());
        }
    }
}
