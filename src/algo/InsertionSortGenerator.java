package algo;

import model.ArrayState;
import steps.CompareStep;
import steps.SetValueStep;
import steps.Step;
import steps.SwapStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class InsertionSortGenerator implements StepGenerator {
    private List<Step> steps;
    private int[] work;

    @Override
    public List<Step> generate(ArrayState initialState) {
        work = Arrays.copyOf(initialState.getData(), initialState.length());
        steps = new ArrayList<>();

        for (int i=1; i < work.length; i++) {
            int temp = work[i];
            int j = i;
            if (work[j - 1] < work[j]) {
                steps.add(new CompareStep(j - 1, j));
            }
            while (j > 0 && temp < work[j - 1]) {
                steps.add(new CompareStep(j, j - 1));

                int swapTemp = work[j];
                work[j] = work[j - 1];
                work[j - 1] = swapTemp;

                steps.add(new SwapStep(j, j - 1));
                j--;
            }
        }

        return steps;
    }

    public static void main(String[] args) {
        int[] testData = {4, 2, 7, 1, 8, 3, 9, 5, 10, 6};
        ArrayState state = new ArrayState(testData);

        InsertionSortGenerator gen = new InsertionSortGenerator();
        List<Step> result = gen.generate(state);

        System.out.println("Generated Steps for [4, 2, 7, 1, 8, 3, 9, 5, 10, 6]:");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(i + ": " + result.get(i).getDescription());
            if (result.get(i) instanceof SetValueStep) {
                System.out.println(Arrays.toString(gen.work));
            }
        }
        System.out.println("\nSorted Array: " + Arrays.toString(gen.work));
    }
}
