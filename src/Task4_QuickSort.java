import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// Source: Swift Data Structure and Algorithms - Azar, E & Alebicto, M. E (2016)
// Adapted for Wine objects, sorted by alcohol content.
// Four pivot strategies: first, last, random, median of three.

public class Task4_QuickSort {

    private static final Random random = new Random();

    public static void main(String[] args) {
        runAll("No Shuffle", false);
        runAll("With Shuffle", true);
    }

    private static void runAll(String label, boolean shuffle) {
        System.out.println("\n====== QuickSort (" + label + ") ======");

        ArrayList<Wine> wines = ImportData.readFile();
        System.out.println("Number of wines loaded: " + wines.size());

        String[] strategies = {
                "First Element as Pivot",
                "Last Element as Pivot",
                "Random Element as Pivot",
                "Median of Three as Pivot"
        };

        for (String strategy : strategies) {
            ArrayList<Wine> copy = new ArrayList<>(wines);
            if (shuffle) Collections.shuffle(copy);

            int[] comparisons = new int[]{0};

            Runtime rt = Runtime.getRuntime();
            rt.gc();
            long memBefore = rt.totalMemory() - rt.freeMemory();
            Timer timer = new Timer();
            timer.start();

            switch (strategy) {
                case "First Element as Pivot"   -> quickSortFirst(copy, 0, copy.size() - 1, comparisons);
                case "Last Element as Pivot"    -> quickSortLast(copy, 0, copy.size() - 1, comparisons);
                case "Random Element as Pivot"  -> quickSortRandom(copy, 0, copy.size() - 1, comparisons);
                case "Median of Three as Pivot" -> quickSortMedian(copy, 0, copy.size() - 1, comparisons);
            }

            timer.stop();
            long memAfter = rt.totalMemory() - rt.freeMemory();
            printResults(strategy, comparisons[0], timer.durationMicros(),
                    memAfter - memBefore, copy);
        }
    }

    // Pivot: First element — swaps it to end, then uses last-element partition
    public static void quickSortFirst(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        if (low < high) {
            int pivotIndex = partitionFirst(wines, low, high, comparisons);
            quickSortFirst(wines, low, pivotIndex - 1, comparisons);
            quickSortFirst(wines, pivotIndex + 1, high, comparisons);
        }
    }

    public static int partitionFirst(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        Collections.swap(wines, low, high);
        return partitionLast(wines, low, high, comparisons);
    }

    // Pivot: Last element — standard partition, smaller elements move left
    public static void quickSortLast(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        if (low < high) {
            int pivotIndex = partitionLast(wines, low, high, comparisons);
            quickSortLast(wines, low, pivotIndex - 1, comparisons);
            quickSortLast(wines, pivotIndex + 1, high, comparisons);
        }
    }

    public static int partitionLast(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        double pivot  = wines.get(high).alcohol();
        int leftIndex = low - 1;

        for (int rightIndex = low; rightIndex < high; rightIndex++) {
            comparisons[0]++;
            if (wines.get(rightIndex).alcohol() < pivot) {
                leftIndex++;
                Collections.swap(wines, leftIndex, rightIndex);
            }
        }

        Collections.swap(wines, leftIndex + 1, high);
        return leftIndex + 1;
    }

    // Pivot: Random element — picks random index, swaps to end, then partitions
    public static void quickSortRandom(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        if (low < high) {
            int pivotIndex = partitionRandom(wines, low, high, comparisons);
            quickSortRandom(wines, low, pivotIndex - 1, comparisons);
            quickSortRandom(wines, pivotIndex + 1, high, comparisons);
        }
    }

    public static int partitionRandom(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        int randomIndex = low + random.nextInt(high - low + 1);
        Collections.swap(wines, randomIndex, high);
        return partitionLast(wines, low, high, comparisons);
    }

    // Pivot: Median of three — compares first, middle and last, picks the median
    public static void quickSortMedian(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        if (low < high) {
            int pivotIndex = partitionMedian(wines, low, high, comparisons);
            quickSortMedian(wines, low, pivotIndex - 1, comparisons);
            quickSortMedian(wines, pivotIndex + 1, high, comparisons);
        }
    }

    public static int partitionMedian(ArrayList<Wine> wines, int low, int high, int[] comparisons) {
        int mid = low + (high - low) / 2;
        double first  = wines.get(low).alcohol();
        double middle = wines.get(mid).alcohol();
        double last   = wines.get(high).alcohol();

        int medianIndex;
        if ((first <= middle && middle <= last) || (last <= middle && middle <= first)) {
            medianIndex = mid;
        } else if ((middle <= first && first <= last) || (last <= first && first <= middle)) {
            medianIndex = low;
        } else {
            medianIndex = high;
        }

        Collections.swap(wines, medianIndex, high);
        return partitionLast(wines, low, high, comparisons);
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

    private static void printResults(String strategy, int comparisons, long micros,
                                     long memBytes, ArrayList<Wine> wines) {
        System.out.println("\nQuickSort - " + strategy + ":");
        System.out.println("Number of comparisons       : " + comparisons);
        System.out.println("Approx. memory used (bytes) : " + memBytes);
        System.out.println("Time taken (microseconds)   : " + micros + " µs");
        System.out.println("Sorted                      : " + isSorted(wines));
        printFirstAndLast(wines);
    }
}