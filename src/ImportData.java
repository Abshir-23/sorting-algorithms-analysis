import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ImportData {

    private static final int ALCOHOL_COLUMN_INDEX = 10;

    public static ArrayList<Wine> readFile() {
        HashSet<Double> seen = new HashSet<>();
        ArrayList<Wine> wines = new ArrayList<>();

        // We load files from the resources folder using the ClassLoader.
        // This is the best way to ensure the code works on all computers.
        readCSV("winequality-red.csv", seen, wines);
        readCSV("winequality-white.csv", seen, wines);

        System.out.println("Total unique alcohol values loaded: " + wines.size());
        return wines;
    }

    private static void readCSV(String fileName, HashSet<Double> seen, ArrayList<Wine> wines) {
        // Look for the file in the resources folder
        InputStream is = ImportData.class.getClassLoader().getResourceAsStream(fileName);

        if (is == null) {
            System.err.println("Error: Could not find file " + fileName + " in resources.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                // Skip the CSV header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] columns = line.split(";");
                if (columns.length <= ALCOHOL_COLUMN_INDEX) continue;

                try {
                    double alcohol = Double.parseDouble(columns[ALCOHOL_COLUMN_INDEX].trim());
                    // Only add unique alcohol values
                    if (seen.add(alcohol)) {
                        wines.add(new Wine(alcohol));
                    }
                } catch (NumberFormatException e) {
                    // Skip rows with invalid data
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
        }
    }
}