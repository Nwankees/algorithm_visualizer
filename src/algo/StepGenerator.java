package algo;

import model.ArrayState;
import steps.Step;

import java.util.List;

public interface StepGenerator<S> {
    List<Step<S>> generate(S initialState);
}
