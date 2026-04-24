package steps;

import model.BstNode;
import model.BstState;
import util.Trees.AvlUtils;

public class RotateLeftStep implements Step<BstState> {
    private final int pivotKey;

    public RotateLeftStep(int pivotKey) {
        this.pivotKey = pivotKey;
    }

    @Override
    public void apply(BstState state) {
        state.setRoot(rotateLeftAt(state.getRoot(), pivotKey));
        state.setHighlightKey(pivotKey);
        state.getCounters().incWrites();
    }

    private BstNode rotateLeftAt(BstNode root, int key) {
        if (root == null) return null;

        if (key < root.getKey()) {
            root.setBstNodeLeft(rotateLeftAt(root.getBstNodeLeft(), key));
        } else if (key > root.getKey()) {
            root.setBstNodeRight(rotateLeftAt(root.getBstNodeRight(), key));
        } else {
            return AvlUtils.rotateLeft(root);
        }

        AvlUtils.updateHeight(root);
        return root;
    }

    @Override
    public StepType getType() {
        return StepType.HIGHLIGHT;
    }

    @Override
    public String getDescription() {
        return "Rotate left at " + pivotKey;
    }
}