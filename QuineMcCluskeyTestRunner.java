import java.io.*;
import java.util.*;

public class QuineMcCluskeyTestRunner {
    public static void main(String[] args) {
        // Path to test cases folder
        String testCasesFolder = "test_cases/";
        String combinedOutputFile = "combined_output.pla";

        File folder = new File(testCasesFolder);
        File[] testFiles = folder.listFiles((dir, name) -> name.endsWith(".pla"));

        if (testFiles == null || testFiles.length == 0) {
            System.out.println("No test cases found in folder: " + testCasesFolder);
            return;
        }

        int total = testFiles.length;

        try (BufferedWriter combinedWriter = new BufferedWriter(new FileWriter(combinedOutputFile))) {
            for (File testFile : testFiles) {
                System.out.println("Running test case: " + testFile.getName());

                // Write header to the combined output file
                combinedWriter.write("# Test case: " + testFile.getName() + "\n");

                // Run the QuineMcCluskey program
                try {
                    Process process = new ProcessBuilder(
                        "java", "QuineMcCluskey", testFile.getPath()
                    ).redirectErrorStream(true).start();

                    // Capture the output
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        combinedWriter.write(line + "\n");
                    }
                    process.waitFor();

                    // Add separator for clarity
                    combinedWriter.write("# End of test case: " + testFile.getName() + "\n\n");
                    System.out.println("Test Finished.");
                } catch (Exception e) {
                    System.out.println("Error running test case: " + e.getMessage());
                }

                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Error writing combined output: " + e.getMessage());
        }

        System.out.println("All test cases completed. Results saved to: " + combinedOutputFile);
    }
}
