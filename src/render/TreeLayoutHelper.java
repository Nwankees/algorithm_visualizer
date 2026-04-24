package render;

import model.BstNode;

import java.util.ArrayList;
import java.util.List;

public class TreeLayoutHelper {
    public List<PositionedNode> computeLayout(BstNode root, double panelWidth){
        List<PositionedNode> positionedNodes = new ArrayList<>();
        if (root == null) {
            return positionedNodes;
        }
        layoutRecursive(root, panelWidth / 2, 60, panelWidth / 4, positionedNodes);
        return positionedNodes;
    }

    private void layoutRecursive(BstNode node, double x, double y, double horizontalGap, List<PositionedNode> positionedNodes) {
        if (node == null) {
            return;
        }
        positionedNodes.add(new PositionedNode(node, x, y));
        layoutRecursive(node.getBstNodeLeft(), x - horizontalGap, y + 80, Math.max(horizontalGap / 2, 30), positionedNodes);
        layoutRecursive(node.getBstNodeRight(), x + horizontalGap, y + 80, Math.max(horizontalGap / 2, 30), positionedNodes);
    }

    public PositionedNode findPosition(BstNode target, List<PositionedNode> positionedNodes) {
        for (PositionedNode node : positionedNodes) {
            if (target.getKey() == node.getNode().getKey()) {
                return node;
            }
        }
        return null;
    }
}
