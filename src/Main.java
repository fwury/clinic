import java.io.*;
import java.util.*;
import java.time.*;

/**
 * The {@code Main} class serves as the entry point for executing the clinic
 * system. It reads the input file path and output file path from the user,
 * initializes the Clinic system, and writes the report to the output file.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Prompt user for the input file path (with a default value fallback)
        System.out.print("Enter path to input file (default: input.txt): ");
        String inputFilePath = scanner.nextLine();
        if (inputFilePath.isEmpty()) {
            inputFilePath = "input.txt";
        }

        // Prompt user for the output file path (with a default value fallback)
        System.out.print("Enter path to output file (default: output.txt): ");
        String outputFilePath = scanner.nextLine();
        if (outputFilePath.isEmpty())
            outputFilePath = "output.txt";

        // Close the scanner to free up resources
        scanner.close();

        // Record the start time to track processing duration
        LocalDateTime startTime = LocalDateTime.now();

        // Initialize the Clinic object with the input file
        File inputFile = new File(inputFilePath);
        System.out.println("\n(1/3) Reading and processing data…");
        Clinic clinic = new Clinic(inputFile);

        // Generate the report
        System.out.println("(2/3) Writing report…");
        String report = clinic.generateReport();

        // Write the report to the output file
        File outputFile = new File(outputFilePath);
        try {
            PrintWriter writer = new PrintWriter(outputFile);
            writer.print(report);
            writer.close();

            // Record the end time after writing completes
            LocalDateTime endTime = LocalDateTime.now();

            // Display confirmation with duration in milliseconds
            System.out.println("(3/3) Report written to " + outputFilePath + " successfully in "
                    + Duration.between(startTime, endTime).toMillis() + " ms");
        } catch (IOException e) {
            // Throw RuntimeException if file writing fails
            throw new RuntimeException("Error writing to file: " + e.getMessage());
        }
    }
}
