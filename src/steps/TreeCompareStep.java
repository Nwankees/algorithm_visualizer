package steps;

import model.BstState;

public class TreeCompareStep implements Step<BstState> {
    private final int currentKey;
    private final int targetKey;

    public TreeCompareStep(int currentKey, int targetKey){
        this.currentKey = currentKey;
        this.targetKey = targetKey;
    }

    public void apply(BstState state) {
        state.setHighlightKey(this.currentKey);
        state.getCounters().incComparisons();
    }
    public StepType getType() {
        return StepType.COMPARE;
    }
    public String getDescription() {
        return "Compare indices " + currentKey + " and " + targetKey;
    }
}
