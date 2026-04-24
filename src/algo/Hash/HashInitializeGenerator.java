package algo.Hash;

import algo.StepGenerator;
import model.HashState;
import steps.Step;

import java.util.ArrayList;
import java.util.List;

public class HashInitializeGenerator implements StepGenerator<HashState> {
    private List<Integer> keysToInsert = new ArrayList<>();

    @Override
    public void setKeysToInsert(List<Integer> keysToInsert) {
        this.keysToInsert = keysToInsert;
    }

    @Override
    public List<Step<HashState>> generate(HashState initialState) {
        List<Step<HashState>> steps = new ArrayList<>();
        HashState simulatedState = new HashState(initialState.getCapacity());

        for (int key : keysToInsert) {
            HashInsertGenerator generator = new HashInsertGenerator();
            generator.setKeyToInsert(key);
            List<Step<HashState>> insertSteps = generator.generate(simulatedState);
            steps.addAll(insertSteps);

            for (Step<HashState> step : insertSteps) {
                step.apply(simulatedState);
            }
        }

        simulatedState.resetHighlight();
        return steps;
    }
}
