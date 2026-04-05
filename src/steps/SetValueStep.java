package steps;

import model.ArrayState;

public class SetValueStep implements Step<ArrayState>{
    private final int index;
    private final int value;

    public SetValueStep(int index, int value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public StepType getType() {
        return StepType.SET_VALUE;
    }

    @Override
    public String getDescription() {
        return "Set index " + index + " = " + value;
    }

    @Override
    public void apply(ArrayState state) {
        state.setHighlights(index, -1);
        state.set(index, value);
    }
}
