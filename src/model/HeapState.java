package model;

import util.Counters;

public class HeapState {
    private int[] data;
    private int size;
    private int highlightA;
    private int highlightB;
    private Counters counters;
    private boolean minHeap;

    public HeapState(int capacity) {
        this.data = new int[Math.max(capacity, 1)];
        this.size = 0;
        this.highlightA = -1;
        this.highlightB = -1;
        this.counters = new Counters();
        this.minHeap = false;
    }

    public HeapState(int[] initialData) {
        this(initialData, false);
    }

    public HeapState(int[] initialData, boolean minHeap) {
        this.data = new int[Math.max(initialData.length + 10, 10)];
        this.size = 0;
        this.highlightA = -1;
        this.highlightB = -1;
        this.counters = new Counters();
        this.minHeap = minHeap;

        for (int value : initialData) {
            add(value);
            heapifyUp(size - 1);
        }
        counters.reset();
        resetHighlights();
    }

    public int getSize() {
        return size;
    }

    public int get(int index) {
        return data[index];
    }

    public int[] getData() {
        return data;
    }

    public int getHighlightA() {
        return highlightA;
    }

    public int getHighlightB() {
        return highlightB;
    }

    public int parent(int i) {
        return (i - 1) / 2;
    }

    public int left(int i) {
        return 2 * i + 1;
    }

    public int right(int i) {
        return 2 * i + 2;
    }

    public void add(int value) {
        if (size >= data.length) {
            grow();
        }
        data[size] = value;
        size++;
        counters.incWrites();
    }

    public void removeLast() {
        if (size <= 0) {
            return;
        }
        size--;
        resetHighlights();
        counters.incWrites();
    }

    public void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        counters.incSwaps();
    }

    public void setHighlights(int a, int b) {
        this.highlightA = a;
        this.highlightB = b;
    }

    public void resetHighlights() {
        this.highlightA = -1;
        this.highlightB = -1;
    }

    public Counters getCounters() {
        return counters;
    }

    public boolean isMinHeap() {
        return minHeap;
    }

    private void heapifyUp(int index) {
        int i = index;
        while (i > 0) {
            int parent = parent(i);
            if (!shouldSwap(data[i], data[parent])) {
                break;
            }
            int temp = data[i];
            data[i] = data[parent];
            data[parent] = temp;
            i = parent;
        }
    }

    private void grow() {
        int[] next = new int[Math.max(data.length * 2, 1)];
        System.arraycopy(data, 0, next, 0, data.length);
        data = next;
    }

    private boolean shouldSwap(int child, int parent) {
        if (minHeap) {
            return child < parent;
        }
        return child > parent;
    }
}
