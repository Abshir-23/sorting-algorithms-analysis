// Represents a wine entry from the UCI Wine Quality Dataset
public record Wine(double alcohol) {
    public Wine {
        if (alcohol < 0) throw new IllegalArgumentException("Alcohol cannot be negative: " + alcohol);
    }
}