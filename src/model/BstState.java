package model;

import util.Counters;

public class BstState {
    private BstNode root;
    private Integer highlightKey = null;
    private Counters counters;

    public BstState(int[] initialData) {
        this.root = null;
        this.counters = new Counters();
    }

    public BstNode getRoot(){
        return this.root;
    };
    public void setRoot(BstNode root){
        this.root = root;
    };
    public Integer getHighlightKey() {
        return this.highlightKey;
    }
    public void setHighlightKey(Integer highlightKey) {
        this.highlightKey = highlightKey;
    }
    public Counters getCounters() {
        return this.counters;
    }
    public void reset() {
        this.root = null;
        this.highlightKey = null;
        counters.reset();
    }
}
