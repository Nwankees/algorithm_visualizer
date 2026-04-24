package util.Trees;

import model.BstNode;
import util.StepRunner;

public class AvlUtils {

    public static int height(BstNode node) {
        return node == null ? 0 : node.getHeight();
    }

    public static int getBalance(BstNode node) {
        if (node == null) return 0;
        return height(node.getBstNodeLeft()) - height(node.getBstNodeRight());
    }

    public static void updateHeight(BstNode node) {
        if (node == null) return;
        node.setHeight(1 + Math.max(height(node.getBstNodeLeft()), height(node.getBstNodeRight())));
    }

    public static BstNode rotateRight(BstNode y) {
        BstNode x = y.getBstNodeLeft();
        BstNode t2 = x.getBstNodeRight();

        x.setBstNodeRight(y);
        y.setBstNodeLeft(t2);

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    public static BstNode rotateLeft(BstNode x) {
        BstNode y = x.getBstNodeRight();
        BstNode t2 = y.getBstNodeLeft();

        y.setBstNodeLeft(x);
        x.setBstNodeRight(t2);

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    public static BstNode find(BstNode root, int key) {
        BstNode current = root;

        while (current != null) {
            if (key == current.getKey()) return current;
            if (key < current.getKey()) current = current.getBstNodeLeft();
            else current = current.getBstNodeRight();
        }

        return null;
    }

    public static boolean isValidAVL(BstNode root) {
        return StepRunner.isValidBST(root) && checkAVL(root) != -1;
    }

    private static int checkAVL(BstNode node) {
        if (node == null) return 0;

        int leftHeight = checkAVL(node.getBstNodeLeft());
        if (leftHeight == -1) return -1;

        int rightHeight = checkAVL(node.getBstNodeRight());
        if (rightHeight == -1) return -1;

        if (Math.abs(leftHeight - rightHeight) > 1) return -1;

        int expectedHeight = 1 + Math.max(leftHeight, rightHeight);
        if (node.getHeight() != expectedHeight) return -1;

        return expectedHeight;
    }
}