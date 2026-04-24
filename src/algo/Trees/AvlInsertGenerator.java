package algo.Trees;

import algo.StepGenerator;
import model.BstNode;
import model.BstState;
import steps.RotateLeftStep;
import steps.RotateRightStep;
import steps.Step;
import steps.TreeCompareStep;
import steps.TreeInsertStep;
import util.Trees.AvlUtils;

import java.util.ArrayList;
import java.util.List;

public class AvlInsertGenerator implements StepGenerator<BstState> {
    private int keyToInsert;
    private List<Step<BstState>> steps;

    @Override
    public void setKeyToInsert(int keyToInsert) {
        this.keyToInsert = keyToInsert;
    }

    @Override
    public List<Step<BstState>> generate(BstState initialState) {
        steps = new ArrayList<>();

        BstNode rootCopy = cloneTree(initialState.getRoot());

        rootCopy = simulateInsert(rootCopy, keyToInsert);

        return steps;
    }

    private BstNode simulateInsert(BstNode node, int key) {
        if (node == null) {
            steps.add(new TreeInsertStep(key));
            return new BstNode(key);
        }

        steps.add(new TreeCompareStep(node.getKey(), key));

        if (key < node.getKey()) {
            node.setBstNodeLeft(simulateInsert(node.getBstNodeLeft(), key));
        } else if (key > node.getKey()) {
            node.setBstNodeRight(simulateInsert(node.getBstNodeRight(), key));
        } else {
            return node;
        }

        AvlUtils.updateHeight(node);

        int balance = AvlUtils.getBalance(node);

        // LL case
        if (balance > 1 && key < node.getBstNodeLeft().getKey()) {
            steps.add(new RotateRightStep(node.getKey()));
            return AvlUtils.rotateRight(node);
        }

        // RR case
        if (balance < -1 && key > node.getBstNodeRight().getKey()) {
            steps.add(new RotateLeftStep(node.getKey()));
            return AvlUtils.rotateLeft(node);
        }

        // LR case
        if (balance > 1 && key > node.getBstNodeLeft().getKey()) {
            steps.add(new RotateLeftStep(node.getBstNodeLeft().getKey()));
            node.setBstNodeLeft(AvlUtils.rotateLeft(node.getBstNodeLeft()));

            steps.add(new RotateRightStep(node.getKey()));
            return AvlUtils.rotateRight(node);
        }

        // RL case
        if (balance < -1 && key < node.getBstNodeRight().getKey()) {
            steps.add(new RotateRightStep(node.getBstNodeRight().getKey()));
            node.setBstNodeRight(AvlUtils.rotateRight(node.getBstNodeRight()));

            steps.add(new RotateLeftStep(node.getKey()));
            return AvlUtils.rotateLeft(node);
        }

        return node;
    }

    private BstNode cloneTree(BstNode root) {
        if (root == null) return null;

        BstNode copy = new BstNode(root.getKey());
        copy.setHeight(root.getHeight());
        copy.setBstNodeLeft(cloneTree(root.getBstNodeLeft()));
        copy.setBstNodeRight(cloneTree(root.getBstNodeRight()));

        return copy;
    }
}