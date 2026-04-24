package steps;

import model.HeapState;

public class HeapInsertStep implements Step<HeapState> {
    private final int value;

    public HeapInsertStep(int value) {
        this.value = value;
    }

    @Override
    public void apply(HeapState state) {
        state.add(value);
        state.setHighlights(state.getSize() - 1, -1);
    }

    @Override
    public StepType getType() {
        return StepType.INSERT_VALUE;
    }

    @Override
    public String getDescription() {
        return "Insert " + value + " into heap";
    }
}
