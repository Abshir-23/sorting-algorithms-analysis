import java.util.ArrayList;
import java.util.Collections;

// Source: Bubble Sort Algorithm - GeeksForGeeks
// https://www.geeksforgeeks.org/bubble-sort-algorithm/


public class Task1_BubbleSort {

    public static void main(String[] args) {

        ArrayList<Wine> wines = ImportData.readFile();
        System.out.println("Number of wines loaded: " + wines.size());

        // Non-optimised on original list
        ArrayList<Wine> nonOptOriginal = new ArrayList<>(wines);
        Runtime rt1 = Runtime.getRuntime();
        rt1.gc();
        long memBefore1 = rt1.totalMemory() - rt1.freeMemory();
        Timer timer1 = new Timer();
        timer1.start();
        int[] result1 = bubbleSortNonOptimised(nonOptOriginal);
        timer1.stop();
        long memAfter1 = rt1.totalMemory() - rt1.freeMemory();
        printResults("Non-Optimised Bubble Sort on original list", result1,
                timer1.durationMicros(), memAfter1 - memBefore1, nonOptOriginal);

        // Optimised on original list
        ArrayList<Wine> optOriginal = new ArrayList<>(wines);
        Runtime rt2 = Runtime.getRuntime();
        rt2.gc();
        long memBefore2 = rt2.totalMemory() - rt2.freeMemory();
        Timer timer2 = new Timer();
        timer2.start();
        int[] result2 = bubbleSortOptimised(optOriginal);
        timer2.stop();
        long memAfter2 = rt2.totalMemory() - rt2.freeMemory();
        printResults("Optimised Bubble Sort on original list", result2,
                timer2.durationMicros(), memAfter2 - memBefore2, optOriginal);

        // Shuffle the list
        shuffle(wines);
        System.out.println("\nThe list has been randomized.");

        // Non-optimised on randomized list
        ArrayList<Wine> nonOptShuffled = new ArrayList<>(wines);
        Runtime rt3 = Runtime.getRuntime();
        rt3.gc();
        long memBefore3 = rt3.totalMemory() - rt3.freeMemory();
        Timer timer3 = new Timer();
        timer3.start();
        int[] result3 = bubbleSortNonOptimised(nonOptShuffled);
        timer3.stop();
        long memAfter3 = rt3.totalMemory() - rt3.freeMemory();
        printResults("Non-Optimised Bubble Sort on randomized list", result3,
                timer3.durationMicros(), memAfter3 - memBefore3, nonOptShuffled);

        // Optimised on randomized list
        ArrayList<Wine> optShuffled = new ArrayList<>(wines);
        Runtime rt4 = Runtime.getRuntime();
        rt4.gc();
        long memBefore4 = rt4.totalMemory() - rt4.freeMemory();
        Timer timer4 = new Timer();
        timer4.start();
        int[] result4 = bubbleSortOptimised(optShuffled);
        timer4.stop();
        long memAfter4 = rt4.totalMemory() - rt4.freeMemory();
        printResults("Optimised Bubble Sort on randomized list", result4,
                timer4.durationMicros(), memAfter4 - memBefore4, optShuffled);
    }

    public static void shuffle(ArrayList<Wine> wines) {
        java.util.Random random = new java.util.Random();
        for (int i = wines.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Wine temp = wines.get(i);
            wines.set(i, wines.get(j));
            wines.set(j, temp);
        }
    }

    public static int[] bubbleSortNonOptimised(ArrayList<Wine> wines) {
        int comparisons = 0;
        int swaps = 0;
        int n = wines.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (wines.get(j).alcohol() > wines.get(j + 1).alcohol()) {
                    Wine temp = wines.get(j);
                    wines.set(j, wines.get(j + 1));
                    wines.set(j + 1, temp);
                    swaps++;
                }
            }
        }

        return new int[]{comparisons, swaps};
    }

    public static int[] bubbleSortOptimised(ArrayList<Wine> wines) {
        int comparisons = 0;
        int swaps = 0;
        int n = wines.size();

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (wines.get(j).alcohol() > wines.get(j + 1).alcohol()) {
                    Wine temp = wines.get(j);
                    wines.set(j, wines.get(j + 1));
                    wines.set(j + 1, temp);
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        return new int[]{comparisons, swaps};
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
        System.out.println("Number of swaps             : " + result[1]);
        System.out.println("Number of comparisons       : " + result[0]);
        System.out.println("Approx. memory used (bytes) : " + memBytes);
        System.out.println("Time taken (microseconds)   : " + micros + " µs");
        System.out.println("Sorted                      : " + isSorted(wines));
        printFirstAndLast(wines);
    }
}