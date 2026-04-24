package steps;

import model.BstNode;
import model.BstState;
import util.Trees.AvlUtils;

public class RotateRightStep implements Step<BstState> {
    private final int pivotKey;

    public RotateRightStep(int pivotKey) {
        this.pivotKey = pivotKey;
    }

    @Override
    public void apply(BstState state) {
        state.setRoot(rotateRightAt(state.getRoot(), pivotKey));
        state.setHighlightKey(pivotKey);
        state.getCounters().incWrites();
    }

    private BstNode rotateRightAt(BstNode root, int key) {
        if (root == null) return null;

        if (key < root.getKey()) {
            root.setBstNodeLeft(rotateRightAt(root.getBstNodeLeft(), key));
        } else if (key > root.getKey()) {
            root.setBstNodeRight(rotateRightAt(root.getBstNodeRight(), key));
        } else {
            return AvlUtils.rotateRight(root);
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
        return "Rotate right at " + pivotKey;
    }
}