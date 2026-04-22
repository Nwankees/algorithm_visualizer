package util;

import model.ArrayState;
import model.BstNode;
import steps.Step;
import steps.StepType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class StepRunner<S>{
    private List<Step<S>> steps;
    private int[] initialData;
    private S state;
    private int index;

    private final Function<int[], S> stateFactory;

    public StepRunner(int[] initialData, List<Step<S>> steps, Function<int[], S> stateFactory) {
        this.initialData = Arrays.copyOf(initialData, initialData.length);
        this.steps = steps;
        this.stateFactory = stateFactory;
        state = stateFactory.apply(Arrays.copyOf(initialData, initialData.length));
        index = 0;
    }

    public S getState() {
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

        Step<S> currentStep = this.steps.get(this.index);

        currentStep.apply(this.state);
        this.index += 1;
    }

    public void reset() {
        this.state = stateFactory.apply(Arrays.copyOf(this.initialData, this.initialData.length));
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
            Step<S> currentStep = steps.get(k);
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

    public Step<S> getCurrentStep() {
        return steps.get(this.index - 1);
    }

    public static boolean isValidBST(BstNode root) {
        return recurseTree(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static boolean recurseTree(BstNode node, int min_val, int max_val) {
        if (node == null){
            return true;
        }
        if (node.getKey() <= min_val || node.getKey() >= max_val) {
            return false;
        }
        return recurseTree(node.getBstNodeLeft(), min_val, node.getKey()) &&
                recurseTree(node.getBstNodeRight(), node.getKey(), max_val);
    }
}
