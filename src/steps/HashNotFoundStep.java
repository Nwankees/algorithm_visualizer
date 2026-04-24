package steps;

import model.HashState;

public class HashNotFoundStep implements Step<HashState> {
    private final int index;
    private final int key;

    public HashNotFoundStep(int index, int key) {
        this.index = index;
        this.key = key;
    }

    @Override
    public void apply(HashState state) {
        state.setHighlightIndex(index);
    }

    @Override
    public StepType getType() {
        return StepType.NOT_FOUND;
    }

    @Override
    public String getDescription() {
        return "Key " + key + " not found; stopped at empty index " + index;
    }
}
