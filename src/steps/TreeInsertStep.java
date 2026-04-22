package steps;

import model.BstNode;
import model.BstState;

public class TreeInsertStep implements Step<BstState> {
    private final int key;

    public TreeInsertStep(int key) {
        this.key = key;
    }

    @Override
    public void apply(BstState state) {
        BstNode newNode = new BstNode(this.key);
        if (state.getRoot() == null) {
            state.setRoot(newNode);
        } else {
            BstNode current = state.getRoot();
            while (true) {
                if (key < current.getKey()) {
                    if (current.getBstNodeLeft() == null) {
                        current.setBstNodeLeft(newNode);
                        break;
                    }
                    current = current.getBstNodeLeft();
                } else {
                    if (current.getBstNodeRight() == null) {
                        current.setBstNodeRight(newNode);
                        break;
                    }
                    current = current.getBstNodeRight();
                }
            }
        }
        state.setHighlightKey(this.key);
        state.getCounters().incWrites();
    }

    @Override
    public StepType getType() {
        return StepType.INSERT_VALUE;
    }

    @Override
    public String getDescription() {
        return "Insert " + this.key + " into the BST";
    }
}
