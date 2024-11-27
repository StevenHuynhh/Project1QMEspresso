import java.io.*;
import java.util.*;

public class QuineMcCluskey {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java QuineMcCluskey <input-file>");
            return;
        }
        String inputFile = args[0];

        try {
            // Parse the file (BLIF or PLA format)
            List<String> minterms = parseInput(inputFile);

            // Simplify using Quine-McCluskey
            Set<String> simplifiedTerms = simplify(minterms);

            // Print the minimized result to console
            System.out.println("Minimized Boolean Expression:");
            for (String term : simplifiedTerms) {
                System.out.println(term);
            }

            // Export the result to a PLA file
            exportToPLA("output.pla", minterms.size(), 1, simplifiedTerms);
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Parse input file in BLIF or PLA format to extract minterms.
     */
    private static List<String> parseInput(String inputFile) throws IOException {
        List<String> minterms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            boolean isPla = inputFile.endsWith(".pla");

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith(".")) {
                    continue; // Skip comments and directives
                }
                if (isPla) {
                    // In PLA, extract minterms (lines with 1 at the end)
                    if (line.endsWith(" 1")) {
                        minterms.add(line.substring(0, line.length() - 2).replace(" ", ""));
                    }
                } else {
                    // In BLIF, handle .names section
                    if (line.startsWith(".names")) {
                        continue; // Skip .names header
                    }
                    if (line.endsWith("1")) {
                        minterms.add(line.substring(0, line.length() - 1));
                    }
                }
            }
        }
        return minterms;
    }

    /**
     * Perform the Quine-McCluskey simplification on the given minterms.
     */
    private static Set<String> simplify(List<String> minterms) {
        // Step 1: Group minterms by the number of 1's
        Map<Integer, List<String>> groups = new HashMap<>();
        for (String minterm : minterms) {
            int onesCount = countOnes(minterm);
            groups.computeIfAbsent(onesCount, k -> new ArrayList<>()).add(minterm);
        }

        // Step 2: Perform the minimization process
        Set<String> primeImplicants = new HashSet<>();
        boolean termsLeft = true;
        Map<String, Boolean> used = new HashMap<>();

        while (termsLeft) {
            termsLeft = false;
            Map<Integer, List<String>> newGroups = new HashMap<>();

            for (int i : groups.keySet()) {
                List<String> group1 = groups.get(i);
                List<String> group2 = groups.getOrDefault(i + 1, new ArrayList<>());

                for (String term1 : group1) {
                    boolean combined = false;
                    for (String term2 : group2) {
                        String combinedTerm = combineTerms(term1, term2);
                        if (combinedTerm != null) {
                            termsLeft = true;
                            combined = true;
                            used.put(term1, true);
                            used.put(term2, true);

                            int combinedCount = countOnes(combinedTerm);
                            newGroups.computeIfAbsent(combinedCount, k -> new ArrayList<>()).add(combinedTerm);
                        }
                    }
                    if (!combined) {
                        primeImplicants.add(term1);
                    }
                }
            }
            groups = newGroups;
        }

        // Add any unused terms as prime implicants
        for (String term : minterms) {
            if (!used.getOrDefault(term, false)) {
                primeImplicants.add(term);
            }
        }

        return primeImplicants;
    }

    /**
     * Count the number of 1's in a binary string.
     */
    private static int countOnes(String term) {
        int count = 0;
        for (char c : term.toCharArray()) {
            if (c == '1') count++;
        }
        return count;
    }

    /**
     * Combine two terms if they differ by only one bit.
     */
    private static String combineTerms(String term1, String term2) {
        StringBuilder combined = new StringBuilder();
        int diffCount = 0;

        for (int i = 0; i < term1.length(); i++) {
            if (term1.charAt(i) == term2.charAt(i)) {
                combined.append(term1.charAt(i));
            } else {
                combined.append('-');
                diffCount++;
            }
        }

        return diffCount == 1 ? combined.toString() : null;
    }

    /**
     * Export the minimized Boolean expressions into a PLA file.
     */
    private static void exportToPLA(String filename, int numInputs, int numOutputs, Set<String> minimizedTerms) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Debug: Print the output file path
            System.out.println("Exporting to: " + filename);

            // Write the PLA header
            writer.write(".i " + numInputs + "\n");
            writer.write(".o " + numOutputs + "\n");

            // Debug: Print the number of minimized terms
            System.out.println("Number of minimized terms: " + minimizedTerms.size());

            // Write each minimized term followed by "1"
            for (String term : minimizedTerms) {
                writer.write(term + " 1\n");
            }

            // End the PLA file
            writer.write(".e\n");

            System.out.println("Minimized Boolean expressions exported to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting to PLA file: " + e.getMessage());
        }
    }
}
