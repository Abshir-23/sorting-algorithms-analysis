import java.util.ArrayList;
import java.util.Collections;

// Source: Insertion Sort Algorithm - GeeksForGeeks
// https://www.geeksforgeeks.org/insertion-sort-algorithm/
// Adapted for Wine objects, sorted by alcohol content.

public class Task2_InsertionSort {

    public static void main(String[] args) {

        ArrayList<Wine> wines = ImportData.readFile();
        System.out.println("Number of wines loaded: " + wines.size());

        // Insertion sort on original list
        ArrayList<Wine> original = new ArrayList<>(wines);
        Runtime rt1 = Runtime.getRuntime();
        rt1.gc();
        long memBefore1 = rt1.totalMemory() - rt1.freeMemory();
        Timer timer1 = new Timer();
        timer1.start();
        int[] result1 = insertionSort(original);
        timer1.stop();
        long memAfter1 = rt1.totalMemory() - rt1.freeMemory();
        printResults("Insertion Sort on original list", result1,
                timer1.durationMicros(), memAfter1 - memBefore1, original);

        // Shuffle the list
        Collections.shuffle(wines);
        System.out.println("\nThe list has been randomized.");

        // Insertion sort on randomized list
        ArrayList<Wine> shuffled = new ArrayList<>(wines);
        Runtime rt2 = Runtime.getRuntime();
        rt2.gc();
        long memBefore2 = rt2.totalMemory() - rt2.freeMemory();
        Timer timer2 = new Timer();
        timer2.start();
        int[] result2 = insertionSort(shuffled);
        timer2.stop();
        long memAfter2 = rt2.totalMemory() - rt2.freeMemory();
        printResults("Insertion Sort on randomized list", result2,
                timer2.durationMicros(), memAfter2 - memBefore2, shuffled);
    }

    public static int[] insertionSort(ArrayList<Wine> wines) {
        int comparisons = 0;
        int shifts = 0;
        int n = wines.size();

        for (int i = 1; i < n; i++) {
            Wine current = wines.get(i);
            int j = i - 1;

            while (j >= 0 && wines.get(j).alcohol() > current.alcohol()) {
                comparisons++;
                wines.set(j + 1, wines.get(j));
                shifts++;
                j--;
            }
            if (j >= 0) comparisons++;
            wines.set(j + 1, current);
        }

        return new int[]{comparisons, shifts};
    }

    private static boolean isSorted(ArrayList<Wine> wines) {
        for (int i = 0; i < wines.size() - 1; i++) {
            if (wines.get(i).alcohol() > wines.get(i + 1).alcohol()) return false;
        }
        return true;
    }

    private static void printFirstAndLast(ArrayList<Wine> wines) {
        System.out.println("First 5 alcohol values:");
        for (int i = 0; i < Math.min(5, wines.size()); i++) {
            System.out.printf("  %.4f%n", wines.get(i).alcohol());
        }
        System.out.println("Last 5 alcohol values:");
        for (int i = Math.max(0, wines.size() - 5); i < wines.size(); i++) {
            System.out.printf("  %.4f%n", wines.get(i).alcohol());
        }
    }

    private static void printResults(String label, int[] result, long micros,
                                     long memBytes, ArrayList<Wine> wines) {
        System.out.println("\n" + label + ":");
        System.out.println("Number of shifts            : " + result[1]);
        System.out.println("Number of comparisons       : " + result[0]);
        System.out.println("Approx. memory used (bytes) : " + memBytes);
        System.out.println("Time taken (microseconds)   : " + micros + " µs");
        System.out.println("Sorted                      : " + isSorted(wines));
        printFirstAndLast(wines);
    }
}
