package algo.Heaps;

import algo.StepGenerator;
import model.HeapState;
import steps.HeapCompareStep;
import steps.HeapInsertStep;
import steps.HeapSwapStep;
import steps.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeapInsertGenerator implements StepGenerator<HeapState> {
    private int value;

    @Override
    public void setKeyToInsert(int keyToInsert) {
        this.value = keyToInsert;
    }

    @Override
    public List<Step<HeapState>> generate(HeapState initialState) {
        List<Step<HeapState>> steps = new ArrayList<>();
        int[] work = Arrays.copyOf(initialState.getData(), initialState.getData().length);
        int size = initialState.getSize();

        steps.add(new HeapInsertStep(value));
        work[size] = value;
        size++;

        int i = size - 1;
        while (i > 0) {
            int parent = (i - 1) / 2;
            steps.add(new HeapCompareStep(i, parent));

            if (work[i] > work[parent]) {
                steps.add(new HeapSwapStep(i, parent));
                int temp = work[i];
                work[i] = work[parent];
                work[parent] = temp;
                i = parent;
            } else {
                break;
            }
        }

        return steps;
    }
}
