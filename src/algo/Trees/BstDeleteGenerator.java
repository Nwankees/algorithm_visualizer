package algo.Trees;

import algo.StepGenerator;
import model.BstNode;
import model.BstState;
import steps.*;
import java.util.ArrayList;
import java.util.List;

public class BstDeleteGenerator implements StepGenerator<BstState> {
    private int keyToDelete;
    private List<Step<BstState>> steps;

    @Override
    public void setKeyToInsert(int key) {
        this.keyToDelete = key;
    }

    @Override
    public List<Step<BstState>> generate(BstState initialState) {
        steps = new ArrayList<>();
        BstNode current = initialState.getRoot();

        while (current != null) {
            steps.add(new TreeCompareStep(current.getKey(), keyToDelete));

            if (keyToDelete == current.getKey()) {
                steps.add(new TreeDeleteStep(keyToDelete));
                break;
            } else if (keyToDelete < current.getKey()) {
                current = current.getBstNodeLeft();
            } else {
                current = current.getBstNodeRight();
            }
        }
        return steps;
    }
}