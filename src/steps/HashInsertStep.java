package steps;

import model.HashState;

public class HashInsertStep implements Step<HashState> {
    private final int index;
    private final int key;

    public HashInsertStep(int index, int key) {
        this.index = index;
        this.key = key;
    }

    @Override
    public void apply(HashState state) {
        state.set(index, key);
        state.setHighlightIndex(index);
    }

    @Override
    public StepType getType() {
        return StepType.INSERT_VALUE;
    }

    @Override
    public String getDescription() {
        return "Insert " + key + " at index " + index;
    }
}
