package algo.Trees;

import algo.StepGenerator;
import model.BstNode;
import model.BstState;
import steps.Step;
import steps.TreeCompareStep;
import java.util.ArrayList;
import java.util.List;

public class BstSearchGenerator implements StepGenerator<BstState> {
    private int keyToSearch;

    @Override
    public void setKeyToInsert(int key) { this.keyToSearch = key; }

    @Override
    public List<Step<BstState>> generate(BstState initialState) {
        List<Step<BstState>> steps = new ArrayList<>();
        BstNode current = initialState.getRoot();
        while (current != null) {
            steps.add(new TreeCompareStep(current.getKey(), keyToSearch));
            if (keyToSearch == current.getKey()) break;
            current = (keyToSearch < current.getKey()) ?
                    current.getBstNodeLeft() : current.getBstNodeRight();
        }
        return steps;
    }
}