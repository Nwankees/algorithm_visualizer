package steps;

import model.ArrayState;

public interface Step<S> {
    void apply(S state);
    StepType getType();
    String getDescription();
}