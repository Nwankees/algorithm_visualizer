package steps;

import model.HashState;

public class HashCollisionStep implements Step<HashState> {
    private final int index;
    private final int key;

    public HashCollisionStep(int index, int key) {
        this.index = index;
        this.key = key;
    }

    @Override
    public void apply(HashState state) {
        state.setHighlightIndex(index);
    }

    @Override
    public StepType getType() {
        return StepType.COLLISION;
    }

    @Override
    public String getDescription() {
        return "Collision at index " + index + " while inserting " + key;
    }
}
