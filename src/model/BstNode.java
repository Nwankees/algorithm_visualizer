package model;

public class BstNode {
    private int key;
    private BstNode left;
    private BstNode right;

    public BstNode(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public BstNode getBstNodeLeft() {
        return this.left;
    }

    public BstNode getBstNodeRight() {
        return this.right;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setBstNodeLeft(BstNode left) {
        this.left = left;
    }

    public void setBstNodeRight(BstNode right) {
        this.right = right;
    }
}
