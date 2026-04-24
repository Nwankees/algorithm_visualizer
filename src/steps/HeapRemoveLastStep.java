package steps;

import model.HeapState;

public class HeapRemoveLastStep implements Step<HeapState> {
    @Override
    public void apply(HeapState state) {
        state.removeLast();
    }

    @Override
    public StepType getType() {
        return StepType.SET_VALUE;
    }

    @Override
    public String getDescription() {
        return "Remove last heap node";
    }
}
