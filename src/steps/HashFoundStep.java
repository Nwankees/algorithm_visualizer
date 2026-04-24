package steps;

import model.HashState;

public class HashFoundStep implements Step<HashState> {
    private final int index;
    private final int key;

    public HashFoundStep(int index, int key) {
        this.index = index;
        this.key = key;
    }

    @Override
    public void apply(HashState state) {
        state.setHighlightIndex(index);
    }

    @Override
    public StepType getType() {
        return StepType.FOUND;
    }

    @Override
    public String getDescription() {
        return "Found " + key + " at index " + index;
    }
}
