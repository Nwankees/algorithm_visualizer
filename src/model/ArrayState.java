package model;

import util.Counters;

public class ArrayState {
    private int[] data;
    private int highlightA;
    private int highlightB;
    private Counters counters;

    public ArrayState(int[] initialData) {
        this.data = initialData;
        this.highlightA = -1;
        this.highlightB = -1;
        this.counters = new Counters();
    }

    public int[] getData() {
        return this.data;
    }

    public int getHighlightA() {
        return this.highlightA;
    }

    public int getHighlightB() {
        return this.highlightB;
    }
    public Counters getCounters() {
        return this.counters;
    }

    public int get(int index) {
        return this.data[index];
    }

    public int length() {
        return this.data.length;
    }

    public void setHighlights(int a, int b) {
        this.highlightA = a;
        this.highlightB = b;
    }

    public void set(int index, int value) {
        this.data[index] = value;
        counters.incWrites();
    }

    public void swap(int i, int j) {
        int temp = data[j];
        data[j] = data[i];
        data[i] = temp;

        counters.incSwaps();
    }

    public void resetHighlights() {
        this.highlightA = -1;
        this.highlightB = -1;
    }
}
