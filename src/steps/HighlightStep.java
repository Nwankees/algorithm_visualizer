package steps;

import model.ArrayState;

public class HighlightStep implements Step{
    private final int a;
    private final int b;

    public HighlightStep(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public StepType getType() {
        return StepType.HIGHLIGHT;
    }

    @Override
    public String getDescription() {
        return "Highlight " + a + ", " + b;
    }

    @Override
    public void apply(ArrayState state) {
        state.setHighlights(a, b);
    }
}
