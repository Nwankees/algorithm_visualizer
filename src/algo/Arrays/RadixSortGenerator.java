package algo.Arrays;

import algo.StepGenerator;
import model.ArrayState;
import steps.ArrayCompareStep;
import steps.SetValueStep;
import steps.Step;

import java.util.*;

public class RadixSortGenerator implements StepGenerator<ArrayState> {
    private int[] work;

    @Override
    public List<Step<ArrayState>> generate(ArrayState initialState) {
        this.work = Arrays.copyOf(initialState.getData(), initialState.length());
        List<Step<ArrayState>> out = new ArrayList<>();

        radix_sort(out);
        return out;
    }

    public void radix_sort(List<Step<ArrayState>> out) {
        int maxNumber = work[0];

        for (int i = 1; i < work.length; i++) {
            maxNumber = Math.max(maxNumber, work[i]);
            out.add(new ArrayCompareStep(i, i - 1));
        }

        int maxNumberLength = Integer.toString(maxNumber).length();

        for (int i = 0; i < maxNumberLength; i++) {
            Queue<Integer> bucket0 = new LinkedList<>();
            Queue<Integer> bucket1 = new LinkedList<>();
            Queue<Integer> bucket2 = new LinkedList<>();
            Queue<Integer> bucket3 = new LinkedList<>();
            Queue<Integer> bucket4 = new LinkedList<>();
            Queue<Integer> bucket5 = new LinkedList<>();
            Queue<Integer> bucket6 = new LinkedList<>();
            Queue<Integer> bucket7 = new LinkedList<>();
            Queue<Integer> bucket8 = new LinkedList<>();
            Queue<Integer> bucket9 = new LinkedList<>();
            ArrayList<Queue<Integer>> buckets = new ArrayList<>();
            buckets.add(bucket0);
            buckets.add(bucket1);
            buckets.add(bucket2);
            buckets.add(bucket3);
            buckets.add(bucket4);
            buckets.add(bucket5);
            buckets.add(bucket6);
            buckets.add(bucket7);
            buckets.add(bucket8);
            buckets.add(bucket9);

            for (int number : work) {
                int divisor = (int) Math.pow(10, i);
                int digit = (number / divisor) % 10;
                buckets.get(digit).add(number);
            }

            work = scanBuckets(buckets, out).stream().mapToInt(Integer::intValue).toArray();
            System.out.println(Arrays.toString(work));
        }

    }

    public ArrayList<Integer> scanBuckets(ArrayList<Queue<Integer>> buckets, List<Step<ArrayState>> out) {
        ArrayList<Integer> newArray = new ArrayList<>();
        int i = 0;
        for (Queue<Integer> bucket : buckets) {
            while (!bucket.isEmpty()) {
                int val = bucket.remove();
                newArray.add(val);
                out.add(new SetValueStep(i, val));
                i++;
            }
        }

        boolean allAreEmpty = true;
        for (Queue<Integer> bucket : buckets) {
            if (bucket.isEmpty()) {
                allAreEmpty = true;
            }
            else {
                allAreEmpty = false;
            }
        }

        System.out.println("Are all the buckets empty?: " + allAreEmpty);

        return newArray;
    }

    public void main(String[] args) {
        int test = 3456;
        Random random = new Random();
        int[] testArr = random.ints(9, 1, 100).toArray();
        System.out.println(Arrays.toString(testArr));
        String hey = "hello";
        int lastDigit = Math.floorMod(test, (int) Math.pow(10, 2));
        System.out.println(Integer.parseInt("5") + 10);

        RadixSortGenerator radixSortGenerator = new RadixSortGenerator();
        ArrayState arrayState = new ArrayState(testArr);

        radixSortGenerator.generate(arrayState);
        System.out.println(Arrays.toString(radixSortGenerator.work));
    }

}
