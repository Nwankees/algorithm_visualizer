package steps;

import model.ArrayState;

public class SwapStep implements Step<ArrayState> {
    private final int i;
    private final int j;

    public SwapStep(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public StepType getType() {
        return StepType.SWAP;
    }

    @Override
    public String getDescription() {
        return "Swap indices " + i + " and " + j;
    }

    @Override
    public void apply(ArrayState state) {
        state.setHighlights(i, j);
        state.swap(i, j);
    }
}
