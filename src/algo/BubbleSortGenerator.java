package algo;

import model.ArrayState;
import steps.CompareStep;
import steps.Step;
import steps.SwapStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleSortGenerator implements StepGenerator{
    private int[] work;
    private List<Step> steps;

    @Override
    public List<Step> generate(ArrayState initialState) {
        work = Arrays.copyOf(initialState.getData(), initialState.getData().length);
        steps = new ArrayList<>();


        for (int i=0; i < work.length - 1; i++) {
            boolean swapped = false;
            for (int j=0; j < work.length - 1 - i; j++) {
                if (work[j + 1] < work[j]) {
                    steps.add(new CompareStep(j+ 1, j));
                    int temp = work[j + 1];
                    work[j + 1] = work[j];
                    work[j] = temp;
                    steps.add(new SwapStep(j + 1, j));
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }

        return steps;
    }

    public static void main(String[] args) {
        int[] work = {5,3,8,4,6,2,9,1,7,10};
        for (int i=0; i < work.length - 1; i++) {
            boolean swapped = false;
            for (int j=0; j < work.length - 1 - i; j++) {
                if (work[j + 1] < work[j]) {
                    int temp = work[j + 1];
                    work[j + 1] = work[j];
                    work[j] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }

        System.out.println(Arrays.toString(work));
    }
}
