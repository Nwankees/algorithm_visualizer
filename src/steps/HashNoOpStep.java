package steps;

import model.HashState;

public class HashNoOpStep implements Step<HashState> {
    private final String description;

    public HashNoOpStep(String description) {
        this.description = description;
    }

    @Override
    public void apply(HashState state) {
    }

    @Override
    public StepType getType() {
        return StepType.NO_OP;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
