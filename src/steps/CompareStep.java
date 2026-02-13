package steps;

import model.ArrayState;

public class CompareStep implements Step{
    private final int i;
    private final int j;

    public CompareStep(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public StepType getType() {
        return StepType.COMPARE;
    }

    @Override
    public String getDescription() {
        return "Compare indices " + i + " and " + j;
    }

    @Override
    public void apply(ArrayState state) {
        state.setHighlights(i, j);
        state.getCounters().incComparisons();
    }
}
