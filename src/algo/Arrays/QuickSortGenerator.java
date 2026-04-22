package algo.Arrays;

import algo.StepGenerator;
import model.ArrayState;
import steps.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSortGenerator implements StepGenerator<ArrayState> {
    private int[] work;

    @Override
    public List<Step<ArrayState>> generate(ArrayState initialState) {
        work = Arrays.copyOf(initialState.getData(), initialState.length());
        List<Step<ArrayState>> out = new ArrayList<>();

        // Start the recursive process
        quickSort(0, work.length - 1, out);

        return out;
    }

    private void quickSort(int low, int high, List<Step<ArrayState>> out) {
        if (low < high) {
            // Highlight the current range being considered
            out.add(new RangeHighlightStep(low, high));
            out.add(new NoOpStep("Picking pivot and partitioning range [" + low + ", " + high + "]"));

            // Partition the array and get the pivot's final home
            int pivotIndex = partition(low, high, out);

            // Sort elements before and after the pivot
            quickSort(low, pivotIndex - 1, out);
            quickSort(pivotIndex + 1, high, out);
        }
    }

    private int partition(int low, int high, List<Step<ArrayState>> out) {
        int pivot = work[high]; // Last element as pivot
        int i = low - 1; // Boundary for the "smaller than pivot" zone

        for (int j = low; j < high; j++) {
            // Compare current element with pivot
            out.add(new ArrayCompareStep(j, high));

            if (work[j] <= pivot) {
                i++;
                // Swap current element into the "short zone"
                swap(i, j, out);
            }
        }

        // Place pivot into its final position
        swap(i + 1, high, out);

        out.add(new NoOpStep("Pivot is now at index " + (i + 1)));
        return i + 1;
    }

    private void swap(int a, int b, List<Step<ArrayState>> out) {
        if (a != b) {
            int temp = work[a];
            work[a] = work[b];
            work[b] = temp;
            // Record the swap for the visualizer
            out.add(new SwapStep(a, b));
        }
    }
}