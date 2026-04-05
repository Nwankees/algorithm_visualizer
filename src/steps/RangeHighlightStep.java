package steps;

import model.ArrayState;

public class RangeHighlightStep implements Step<ArrayState>{
    private int left;
    private int right;

    public RangeHighlightStep(int left, int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public StepType getType() {
        return StepType.RANGE_HIGHLIGHT;
    }

    @Override
    public String getDescription() {
        return "Highlight range [" + left + ", " + right + "]";
    }

    @Override
    public void apply(ArrayState state) {
//        state.setHighlights(Math.min(left, right), Math.max(left, right));
        state.setRange(Math.min(left, right), Math.max(left, right));
    }
}
