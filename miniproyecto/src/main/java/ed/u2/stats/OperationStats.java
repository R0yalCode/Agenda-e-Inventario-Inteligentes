package ed.u2.stats;

public class OperationStats {

    private long timeNs;
    private long comparisons;
    private long swaps;

    private boolean found;
    private int index;
    private int[] indices; // para findAll

    public OperationStats() {}

    public void setTime(long t) { this.timeNs = t; }
    public void setComparisons(long c) { this.comparisons = c; }
    public void setSwaps(long s) { this.swaps = s; }

    public void setFound(boolean f) { this.found = f; }
    public void setIndex(int idx) { this.index = idx; }
    public void setIndices(int[] arr) { this.indices = arr; }

    public long getTime() { return timeNs; }
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }

    public boolean isFound() { return found; }
    public int getIndex() { return index; }
    public int[] getIndices() { return indices; }
}
