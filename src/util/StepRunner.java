package util;

import model.ArrayState;
import steps.Step;
import steps.StepType;

import java.util.Arrays;
import java.util.List;

public class StepRunner{
    private List<Step> steps;
    private int[] initialData;
    private ArrayState state;
    private int index;

    public StepRunner(int[] initialData, List<Step> steps) {
        this.initialData = Arrays.copyOf(initialData, initialData.length);
        this.steps = steps;
        state = new ArrayState(Arrays.copyOf(initialData, initialData.length));
        index = 0;
    }

    public ArrayState getState() {
        return this.state;
    }

    public int getIndex() {
        return this.index;
    }

    public int getTotalSteps() {
        return this.steps.size();
    }

    public boolean hasPrev() {
        return this.index > 0;
    }

    public boolean hasNext() {
        return this.index < this.getTotalSteps();
    }

    public void next() {
        if (!this.hasNext()) {
            System.out.println("Next can't be called at the end of an array");
            return;
        }

        Step currentStep = this.steps.get(this.index);

        currentStep.apply(this.state);
        this.index += 1;
    }

    public void reset() {
        this.state = new ArrayState(Arrays.copyOf(this.initialData, this.initialData.length));
//        this.state.getCounters().reset();
//        this.state.resetHighlights(); Because reinstantiating state already resets the counters and highlights
        this.index = 0;
    }

    public void prev() {
        if (!this.hasPrev()) {
            System.out.println("Prev can't be called at beginning of array");
            return;
        }

        int target = this.index - 1;
        this.reset();
        for (int k=0; k < target ; k++){
            Step currentStep = steps.get(k);
            currentStep.apply(this.state);
        }
        this.index = target;
    }

    public static boolean isSorted(int[] arr) {
        for (int i=0; i < arr.length - 1; i++) {
            if (arr[i + 1] < arr[i]) {
                return false;
            }
        }

        return true;
    }

    public Step getCurrentStep() {
        return steps.get(this.index - 1);
    }
}
