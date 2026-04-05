package algo;

import model.ArrayState;
import steps.CompareStep;
import steps.SetValueStep;
import steps.Step;
import steps.SwapStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HeapSortGenerator implements StepGenerator<ArrayState> {
    private int[] work;

    public List<Step<ArrayState>> generate(ArrayState initialState) {
        work = Arrays.copyOf(initialState.getData(), initialState.length());
        List<Step<ArrayState>> out = new ArrayList<>();

        heapify(out);
        sort(out);
        return out;
    }

    public void heapify(List<Step<ArrayState>> out) {
        int indexNode = (Math.floorDiv(work.length, 2)) - 1;

        for (int i = indexNode; i >= 0; i--) {
            siftDown(i, work.length, out);
        }
    }

    private void siftDown(int i, int heapSize, List<Step<ArrayState>> out) {
        int parent = i;

        while (true) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            int largest = parent;

            // Compare Left Child
            if (left < heapSize) {
                out.add(new CompareStep(left, largest)); // Log the comparison
                if (work[left] > work[largest]) {
                    largest = left;
                }
            }

            // Compare Right Child
            if (right < heapSize) {
                out.add(new CompareStep(right, largest)); // Log the comparison
                if (work[right] > work[largest]) {
                    largest = right;
                }
            }

            if (largest == parent) {
                break;
            }

            swap(largest, parent, out);
            parent = largest;
        }
    }

    private void swap(int i, int j, List<Step<ArrayState>> out) {
        if (i != j) {
            int temp = work[i];
            work[i] = work[j];
            work[j] = temp;

            out.add(new SwapStep(i, j));
        }
    }

    public void sort(List<Step<ArrayState>> out) {
        for (int i = work.length - 1; i > 0; i--) {
            swap(i, 0, out);
            siftDown(0, i, out);
        }
    }

    public void main(String[] args) {
        Random rand = new Random();
        int[] arr = rand.ints(rand.nextInt(10), 1, 100).toArray();
        ArrayState arrayState = new ArrayState(arr);
        HeapSortGenerator heapSortGenerator = new HeapSortGenerator();
        heapSortGenerator.generate(arrayState);
        System.out.println(Arrays.toString(heapSortGenerator.work));

//        List<Step> out = new ArrayList<>();
//        int[] sortedArr = heapify(arr, out);
//
//        System.out.println(Arrays.toString(sortedArr));
    }
}
