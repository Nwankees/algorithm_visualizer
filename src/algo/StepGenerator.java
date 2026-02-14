package algo;

import model.ArrayState;
import steps.Step;

import java.util.List;

public interface StepGenerator {
    List<Step> generate(ArrayState initialState);
}
