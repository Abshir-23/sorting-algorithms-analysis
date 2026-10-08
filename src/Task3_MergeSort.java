import java.util.ArrayList;
import java.util.Collections;

// Source: Merge Sort Algorithm - GeeksForGeeks
// https://www.geeksforgeeks.org/merge-sort/
// Adapted for Wine objects, sorted by alcohol content.


public class Task3_MergeSort {

    public static void main(String[] args) {

        ArrayList<Wine> wines = ImportData.readFile();
        System.out.println("Number of wines loaded: " + wines.size());

        // Merge sort on original list
        ArrayList<Wine> original = new ArrayList<>(wines);
        Runtime rt1 = Runtime.getRuntime();
        rt1.gc();
        long memBefore1 = rt1.totalMemory() - rt1.freeMemory();
        Timer timer1 = new Timer();
        timer1.start();
        int[] result1 = mergeSort(original, 0, original.size() - 1);
        timer1.stop();
        long memAfter1 = rt1.totalMemory() - rt1.freeMemory();
        printResults("Merge Sort on original list", result1,
                timer1.durationMicros(), memAfter1 - memBefore1, original);

        // Shuffle the list
        Collections.shuffle(wines);
        System.out.println("\nThe list has been randomized.");

        // Merge sort on randomized list
        ArrayList<Wine> shuffled = new ArrayList<>(wines);
        Runtime rt2 = Runtime.getRuntime();
        rt2.gc();
        long memBefore2 = rt2.totalMemory() - rt2.freeMemory();
        Timer timer2 = new Timer();
        timer2.start();
        int[] result2 = mergeSort(shuffled, 0, shuffled.size() - 1);
        timer2.stop();
        long memAfter2 = rt2.totalMemory() - rt2.freeMemory();
        printResults("Merge Sort on randomized list", result2,
                timer2.durationMicros(), memAfter2 - memBefore2, shuffled);
    }

    // Recursively splits the list in half, sorts each half, then merges them back
    public static int[] mergeSort(ArrayList<Wine> wines, int left, int right) {
        if (left >= right) return new int[]{0, 0}; // single element, nothing to do

        int mid = left + (right - left) / 2;

        int[] leftResult  = mergeSort(wines, left, mid);
        int[] rightResult = mergeSort(wines, mid + 1, right);

        int mergeCount   = leftResult[0] + rightResult[0];
        int comparisons  = leftResult[1] + rightResult[1];

        // Merge the two sorted halves and count this merge operation
        comparisons += merge(wines, left, mid, right);
        mergeCount++;

        return new int[]{mergeCount, comparisons};
    }

    // Merges two sorted sublists back into the original list, returns comparison count
    private static int merge(ArrayList<Wine> wines, int left, int mid, int right) {
        ArrayList<Wine> temp = new ArrayList<>();
        int i = left;
        int j = mid + 1;
        int comparisons = 0;

        // Pick the smaller of the two current elements and add it to temp
        while (i <= mid && j <= right) {
            comparisons++;
            if (wines.get(i).alcohol() <= wines.get(j).alcohol()) {
                temp.add(wines.get(i++));
            } else {
                temp.add(wines.get(j++));
            }
        }

        // Copy any remaining elements from the left half
        while (i <= mid)   temp.add(wines.get(i++));

        // Copy any remaining elements from the right half
        while (j <= right) temp.add(wines.get(j++));

        // Write the sorted result back into the original list
        for (int k = 0; k < temp.size(); k++) {
            wines.set(left + k, temp.get(k));
        }

        return comparisons;
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
        System.out.println("Number of merges            : " + result[0]);
        System.out.println("Number of comparisons       : " + result[1]);
        System.out.println("Approx. memory used (bytes) : " + memBytes);
        System.out.println("Time taken (microseconds)   : " + micros + " µs");
        System.out.println("Sorted                      : " + isSorted(wines));
        printFirstAndLast(wines);
    }
}