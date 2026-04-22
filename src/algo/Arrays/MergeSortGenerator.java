package algo.Arrays;

import algo.StepGenerator;
import model.ArrayState;
import steps.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSortGenerator implements StepGenerator<ArrayState> {
    private int[] work;
//    private List<Step> steps;

    @Override
    public List<Step<ArrayState>> generate(ArrayState initialState) {
        work = Arrays.copyOf(initialState.getData(), initialState.length());
        List<Step<ArrayState>> out = new ArrayList<>();

        mergeSort(work, 0, work.length - 1, out);
        return out;
    }

    private void mergeSort(int[] work, int left, int right, List<Step<ArrayState>> out) {
        out.add(new RangeHighlightStep(left, right));
        if (left >= right) {
            return;
        }

        int mid = Math.floorDiv(left + right, 2);
        out.add(new NoOpStep("Dividing range [" + left + ", " + right + "] at index " + mid));

        mergeSort(work, left, mid, out);
        mergeSort(work, mid + 1, right, out);

        out.add(new NoOpStep("Merging sorted halves back into indices " + left + " through " + right));
        merge(work, left, mid, right, out);
    }

    private void merge(int[] work, int left, int mid, int right, List<Step<ArrayState>> out) {
        int[] leftPart = Arrays.copyOfRange(work, left, mid + 1);
        int[] rightPart = Arrays.copyOfRange(work, mid + 1, right + 1);

        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftPart.length && j < rightPart.length) {
            if (leftPart[i] <= rightPart[j]) {
                out.add(new ArrayCompareStep(mid + 1+ j, left + i)); // offset from temp to main array's indices
                work[k] = leftPart[i];
                out.add(new SetValueStep(k, leftPart[i]));
                i += 1;
            }
            else {
                out.add(new ArrayCompareStep(left + i, mid + 1+ j));
                work[k] = rightPart[j];
                out.add(new SetValueStep(k, rightPart[j]));
                j += 1;
            }
            k += 1;
        }

        while (i < leftPart.length) {
            work[k] = leftPart[i];
            out.add(new SetValueStep(k, leftPart[i]));
            i += 1;
            k += 1;
        }

        while (j < rightPart.length) {
            work[k] = rightPart[j];
            out.add(new SetValueStep(k, rightPart[j]));
            j += 1;
            k += 1;
        }
    }
}
