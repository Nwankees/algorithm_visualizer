package algo.Trees;

import algo.StepGenerator;
import model.BstNode;
import model.BstState;
import steps.Step;
import steps.TreeCompareStep;
import steps.TreeInsertStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BstInsertGenerator implements StepGenerator<BstState> {
    private int[] work;
    private List<Step<BstState>> steps;
    private BstNode rootCopy;
    private int keyToInsert;

    @Override
    public void setKeyToInsert(int keyToInsert) {
        this.keyToInsert = keyToInsert;
    }

public List<Step<BstState>> generate(BstState initialState) {
    BstNode current = initialState.getRoot(); // We just READ from here
    steps = new ArrayList<>();

    if (current == null) {
        steps.add(new TreeInsertStep(keyToInsert));
        return steps;
    }

    while (true) {
        steps.add(new TreeCompareStep(current.getKey(), keyToInsert));

        if (keyToInsert < current.getKey()) {
            if (current.getBstNodeLeft() == null) {
                // DO NOT mutate current.setBstNodeLeft here!
                steps.add(new TreeInsertStep(keyToInsert));
                return steps;
            }
            current = current.getBstNodeLeft();
        } else if (keyToInsert > current.getKey()) {
            if (current.getBstNodeRight() == null) {
                // DO NOT mutate here!
                steps.add(new TreeInsertStep(keyToInsert));
                return steps;
            }
            current = current.getBstNodeRight();
        } else {
            return steps; // Handle duplicate key (do nothing)
        }
    }
}
}
