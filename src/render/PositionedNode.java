package render;

import model.BstNode;

public class PositionedNode {
    private final BstNode node;
    private final double x;
    private final double y;

    public PositionedNode(BstNode node, double x, double y) {
        this.node = node;
        this.x = x;
        this.y = y;
    }

    public BstNode getNode() {
        return this.node;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
