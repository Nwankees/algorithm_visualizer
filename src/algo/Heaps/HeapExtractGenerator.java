package algo.Heaps;

import algo.StepGenerator;
import model.HeapState;
import steps.HeapCompareStep;
import steps.HeapRemoveLastStep;
import steps.HeapSwapStep;
import steps.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeapExtractGenerator implements StepGenerator<HeapState> {
    private boolean minHeap;

    public HeapExtractGenerator() {
        this(false);
    }

    public HeapExtractGenerator(boolean minHeap) {
        this.minHeap = minHeap;
    }

    @Override
    public List<Step<HeapState>> generate(HeapState initialState) {
        List<Step<HeapState>> steps = new ArrayList<>();
        int[] work = Arrays.copyOf(initialState.getData(), initialState.getData().length);
        int size = initialState.getSize();

        if (size == 0) {
            return steps;
        }

        if (size == 1) {
            steps.add(new HeapRemoveLastStep());
            return steps;
        }

        steps.add(new HeapSwapStep(0, size - 1));
        swap(work, 0, size - 1);
        steps.add(new HeapRemoveLastStep());
        size--;

        int parent = 0;
        while (true) {
            int left = 2 * parent + 1;
            int right = 2 * parent + 2;

            if (left >= size) {
                break;
            }

            int selectedChild = left;
            if (right < size) {
                steps.add(new HeapCompareStep(left, right));
                if (betterChild(work[right], work[left])) {
                    selectedChild = right;
                }
            }

            steps.add(new HeapCompareStep(parent, selectedChild));
            if (violatesHeapOrder(work[parent], work[selectedChild])) {
                steps.add(new HeapSwapStep(parent, selectedChild));
                swap(work, parent, selectedChild);
                parent = selectedChild;
            } else {
                break;
            }
        }

        return steps;
    }

    private void swap(int[] data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private boolean betterChild(int candidate, int current) {
        if (minHeap) {
            return candidate < current;
        }
        return candidate > current;
    }

    private boolean violatesHeapOrder(int parent, int child) {
        if (minHeap) {
            return parent > child;
        }
        return parent < child;
    }
}
