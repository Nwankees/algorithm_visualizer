package steps;

import model.HeapState;

public class HeapSwapStep implements Step<HeapState> {
    private final int i;
    private final int j;

    public HeapSwapStep(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public void apply(HeapState state) {
        state.setHighlights(i, j);
        state.swap(i, j);
    }

    @Override
    public StepType getType() {
        return StepType.SWAP;
    }

    @Override
    public String getDescription() {
        return "Swap heap indices " + i + " and " + j;
    }
}
