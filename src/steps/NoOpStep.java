package steps;

import model.ArrayState;

public class NoOpStep implements Step<ArrayState>{
    private String description;

    public NoOpStep(String description) {
        this.description = description;
    }

    @Override
    public StepType getType() {
        return StepType.NO_OP;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void apply(ArrayState state) {

    }
}
