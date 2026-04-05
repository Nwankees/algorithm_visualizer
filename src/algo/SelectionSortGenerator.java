package algo;

import model.ArrayState;
import org.w3c.dom.ranges.Range;
import steps.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionSortGenerator implements StepGenerator<ArrayState>{
    private List<Step<ArrayState>> steps;

    @Override
    public List<Step<ArrayState>> generate(ArrayState arrayState) {
        steps = new ArrayList<>();
        int[] state = arrayState.getData();

        for (int i=0; i < state.length - 1; i++) {
            int min_index = i;
            steps.add(new RangeHighlightStep(i + 1, state.length - 1));
            for (int j=i + 1; j < state.length; j++) {
                steps.add(new CompareStep(j, i));
                if (state[j] < state[min_index]) {
                    min_index = j;
//                    steps.add(new HighlightStep(j, j));
                }
            }
            int temp = state[min_index];
            state[min_index] = state[i];
            steps.add(new SetValueStep(min_index, state[i]));
            state[i] = temp;
            steps.add(new SetValueStep(i, temp));
        }

        return steps;
    }

    public static void main(String[] args) {
        int[] state = {64, 34, 25, 5, 22, 11, 90, 12};
        for (int i=0; i < state.length - 1; i++) {
            int min_index = i;
            for (int j=i + 1; j < state.length; j++) {
                if (state[j] < state[min_index]) {
                    min_index = j;
                }
            }
            int temp = state[min_index];
            state[min_index] = state[i];
            state[i] = temp;
        }

        System.out.println(Arrays.toString(state));
    }
}
