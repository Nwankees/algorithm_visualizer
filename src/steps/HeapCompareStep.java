package steps;

import model.HeapState;

public class HeapCompareStep implements Step<HeapState> {
    private final int i;
    private final int j;

    public HeapCompareStep(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public void apply(HeapState state) {
        state.setHighlights(i, j);
        state.getCounters().incComparisons();
    }

    @Override
    public StepType getType() {
        return StepType.COMPARE;
    }

    @Override
    public String getDescription() {
        return "Compare heap indices " + i + " and " + j;
    }
}
