package steps;

import model.HashState;

public class HashProbeStep implements Step<HashState> {
    private final int index;
    private final int key;

    public HashProbeStep(int index, int key) {
        this.index = index;
        this.key = key;
    }

    @Override
    public void apply(HashState state) {
        state.setHighlightIndex(index);
        state.getCounters().incComparisons();
    }

    @Override
    public StepType getType() {
        return StepType.PROBE;
    }

    @Override
    public String getDescription() {
        return "Probe index " + index + " for key " + key;
    }
}
