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
    private List<Integer> keysToInsert;

//    public BstInsertGenerator(List<Integer> keysToInsert) {
//        this.keysToInsert = keysToInsert;
//    }

    @Override
    public void setKeysToInsert(List<Integer> keysToInsert) {
        this.keysToInsert = keysToInsert;
    }

    public List<Step<BstState>> generate(BstState initialState) {
        BstNode rootCopy = null;
//        work = initialState.getRoot();
        steps = new ArrayList<>();
        for (int targetKey : this.keysToInsert) {
            if (rootCopy == null) {
                steps.add(new TreeInsertStep(targetKey));
                rootCopy = new BstNode(targetKey);
                continue;
            }
            BstNode newNode = new BstNode(targetKey);
            BstNode current = rootCopy;
            while (true) {
                if (targetKey < current.getKey()) {
                    steps.add(new TreeCompareStep(current.getKey(), targetKey));
                    if (current.getBstNodeLeft() == null) {
                        current.setBstNodeLeft(newNode);
                        steps.add(new TreeInsertStep(targetKey));
                        break;
                    }
                    current = current.getBstNodeLeft();
                } else {
                    steps.add(new TreeCompareStep(current.getKey(), targetKey));
                    if (current.getBstNodeRight() == null) {
                        current.setBstNodeRight(newNode);
                        steps.add(new TreeInsertStep(targetKey));
                        break;
                    }
                    current = current.getBstNodeRight();
                }
            }
        }
        return steps;
    }


}
