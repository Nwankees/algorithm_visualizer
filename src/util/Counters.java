package util;

// TODO: Writes counter
public class Counters {
    private long comparisons;
    private long swaps;
    private long writes;

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public long getWrites() {
        return writes;
    }

    public void incComparisons() {
        this.comparisons += 1;
    }

    public void incSwaps() {
        this.swaps += 1;
    }

    public void incWrites() {
        this.writes += 1;
    }

    public void reset() {
        this.comparisons = 0;
        this.swaps = 0;
        this.writes = 0;
    }
}
