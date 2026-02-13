package steps;

import model.ArrayState;

public interface Step {
    void apply(ArrayState state);
    StepType getType();
    String getDescription();
}
