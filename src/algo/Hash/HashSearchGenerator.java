package algo.Hash;

import algo.StepGenerator;
import model.HashState;
import steps.HashCollisionStep;
import steps.HashFoundStep;
import steps.HashNoOpStep;
import steps.HashNotFoundStep;
import steps.HashProbeStep;
import steps.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HashSearchGenerator implements StepGenerator<HashState> {
    private int keyToInsert;

    @Override
    public void setKeyToInsert(int keyToInsert) {
        this.keyToInsert = keyToInsert;
    }

    @Override
    public List<Step<HashState>> generate(HashState initialState) {
        List<Step<HashState>> steps = new ArrayList<>();
        Integer[] table = Arrays.copyOf(initialState.getTable(), initialState.getCapacity());
        int capacity = initialState.getCapacity();
        int index = Math.floorMod(keyToInsert, capacity);
        int probes = 0;

        while (probes < capacity) {
            steps.add(new HashProbeStep(index, keyToInsert));

            if (table[index] == null) {
                steps.add(new HashNotFoundStep(index, keyToInsert));
                return steps;
            }

            if (table[index].equals(keyToInsert)) {
                steps.add(new HashFoundStep(index, keyToInsert));
                return steps;
            }

            steps.add(new HashCollisionStep(index, keyToInsert));
            index = (index + 1) % capacity;
            probes++;
        }

        steps.add(new HashNoOpStep("Searched full table; key not found"));
        return steps;
    }
}
