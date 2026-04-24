package steps;

import model.BstNode;
import model.BstState;

public class TreeDeleteStep implements Step<BstState> {
    private final int keyToDelete;

    public TreeDeleteStep(int keyToDelete) { this.keyToDelete = keyToDelete; }

    @Override
    public void apply(BstState state) {
        state.setRoot(deleteRecursive(state.getRoot(), keyToDelete));
        state.setHighlightKey(null);
        state.getCounters().incWrites();
    }

    private BstNode deleteRecursive(BstNode root, int key) {
        if (root == null) return null;
        if (key < root.getKey()) {
            root.setBstNodeLeft(deleteRecursive(root.getBstNodeLeft(), key));
        } else if (key > root.getKey()) {
            root.setBstNodeRight(deleteRecursive(root.getBstNodeRight(), key));
        } else {
            if (root.getBstNodeLeft() == null) return root.getBstNodeRight();
            else if (root.getBstNodeRight() == null) return root.getBstNodeLeft();
            root.setKey(minValue(root.getBstNodeRight()));
            root.setBstNodeRight(deleteRecursive(root.getBstNodeRight(), root.getKey()));
        }
        return root;
    }

    private int minValue(BstNode root) {
        int minv = root.getKey();
        while (root.getBstNodeLeft() != null) {
            minv = root.getBstNodeLeft().getKey();
            root = root.getBstNodeLeft();
        }
        return minv;
    }

    @Override
    public StepType getType() { return StepType.HIGHLIGHT; }
    @Override
    public String getDescription() { return "Delete " + keyToDelete; }
}