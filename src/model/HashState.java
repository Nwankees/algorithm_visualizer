package model;

import util.Counters;

public class HashState {
    private Integer[] table;
    private int size;
    private int highlightIndex;
    private Counters counters;

    public HashState(int capacity) {
        this.table = new Integer[capacity];
        this.size = 0;
        this.highlightIndex = -1;
        this.counters = new Counters();
    }

    public HashState(Integer[] initialTable) {
        this.table = new Integer[initialTable.length];
        this.size = 0;
        this.highlightIndex = -1;
        this.counters = new Counters();

        for (int i = 0; i < initialTable.length; i++) {
            this.table[i] = initialTable[i];
            if (initialTable[i] != null) {
                this.size++;
            }
        }
    }

    public Integer[] getTable() {
        return table;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return table.length;
    }

    public Integer get(int index) {
        return table[index];
    }

    public void set(int index, Integer value) {
        if (table[index] == null && value != null) {
            size++;
        } else if (table[index] != null && value == null) {
            size--;
        }
        table[index] = value;
        counters.incWrites();
    }

    public int hash(int key) {
        return Math.floorMod(key, table.length);
    }

    public double getLoadFactor() {
        return (double) size / table.length;
    }

    public int getHighlightIndex() {
        return highlightIndex;
    }

    public void setHighlightIndex(int index) {
        this.highlightIndex = index;
    }

    public void resetHighlight() {
        this.highlightIndex = -1;
    }

    public Counters getCounters() {
        return counters;
    }
}
