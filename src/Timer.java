// Measures execution time in microseconds using System.nanoTime()
public class Timer {
    private long startTime;
    private long endTime;
    private boolean running;

    public void start() {
        startTime = System.nanoTime();
        running = true;
    }

    public void stop() {
        endTime = System.nanoTime();
        running = false;
    }

    public long durationMicros() {
        if (running) {
            return (System.nanoTime() - startTime) / 1_000; // Still running
        }
        return (endTime - startTime) / 1_000;
    }
}