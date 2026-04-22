package app.ui;

import algo.*;
import algo.Arrays.*;
import algo.Trees.BstInsertGenerator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.ArrayState;
import model.BstNode;
import model.BstState;
import steps.*;
import util.StepRunner;

import java.util.*;

public class MainController {
    @FXML
    private Button btnLoad;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnPause;
    @FXML
    private Label lblComparisons;
    @FXML
    private Label lblSwaps;
    @FXML
    private Label lblStepCount;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblWrites;
    @FXML
    private Label lblNoOps;
    @FXML
    private Pane renderPane;
    @FXML
    private ComboBox<String> cmbAlgorithm;
    @FXML
    private ComboBox<String> cmbViewMode;

    private StepRunner<ArrayState> arrayRunner;
    private StepRunner<BstState> bstRunner;
    private Timeline timeline;

    public void initialize() {
        cmbAlgorithm.getItems().addAll("Insertion Sort", "Merge Sort", "Bubble Sort", "Selection Sort", "Quick Sort", "Heap Sort", "Radix Sort");
        cmbViewMode.getItems().addAll("Array", "Trees");

        cmbAlgorithm.getSelectionModel().selectFirst();
        cmbViewMode.getSelectionModel().selectFirst();
    }

    public void onLoad() {
        System.out.println("Loaded!");
//        this.initDemo();
//        this.LoadInsertionSortDemo(5);
        this.lblStatus.setText("Not sorted");
        this.loadStructure(100);
    }

    public void onReset() {
        if (arrayRunner == null) {
            System.out.println("Load array first!");
            return;
        }
        arrayRunner.reset();
        render();
    }

    public void onPrev() {
        if (arrayRunner == null) {
            System.out.println("Load array first!");
            return;
        }
        if (arrayRunner.hasPrev()) {
            arrayRunner.prev();
            render();
            if (Objects.equals(lblStatus.getText(), "Sorted")) {
                lblStatus.setText("Not sorted ");
            }
        }
    }

    public void onNext() {
        switch (cmbViewMode.getSelectionModel().getSelectedItem()) {
            case "Array":
                if (arrayRunner == null) {
                    System.out.println("Load array first!");
                    return;
                }
                if (arrayRunner.hasNext()) {
                    arrayRunner.next();
                    if (arrayRunner.getCurrentStep() instanceof NoOpStep) {
                        lblNoOps.setText(arrayRunner.getCurrentStep().getDescription());
                    }
                    else { lblNoOps.setText(""); }
                    System.out.println(arrayRunner.getCurrentStep().getDescription());
                    render();
                    if (!arrayRunner.hasNext()) {
                        if (StepRunner.isSorted(arrayRunner.getState().getData())) {
                            lblStatus.setText("Sorted");
                        }
                    }
                }
                break;

            case "Trees":
                if (bstRunner == null) {
                    System.out.println("Load tree first!");
                    return;
                }
                if (bstRunner.hasNext()) {
                    bstRunner.next();
                    if (StepRunner.isValidBST(bstRunner.getState().getRoot()))
//                    if (bstRunner.getCurrentStep() instanceof NoOpStep) {
//                        lblNoOps.setText(bstRunner.getCurrentStep().getDescription());
//                    }
//                    else { lblNoOps.setText(""); }
                    System.out.println(bstRunner.getCurrentStep().getDescription());
                    render();
                }
                break;
        }
    }

    public void onPlay() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
//                if (arrayRunner.hasNext()) {
//                    arrayRunner.next();
//                    render();
//                }
            this.onNext();
        }
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        System.out.println("Playing");
    }

    public void onPause() {
        System.out.println("Paused!");
    }

    public void drawRectangle(double x, double y, double width, double height, Color color, Pane drawingPane) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(color);
        drawingPane.getChildren().add(rectangle);
    }

    private void render() {
        String structureOptionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();

        System.out.println(structureOptionSelected);
        ArrayState state = arrayRunner.getState();
        renderPane.getChildren().clear();

        if (structureOptionSelected.equals("Array")) {
            // 1. Get current dimensions of the drawing area
            double paneWidth = renderPane.getWidth();
            double paneHeight = renderPane.getHeight();
            int n = state.getData().length;

            // 2. Calculate dynamic width
            double barWidth = paneWidth / n;

            // 3. Find Max Value for height scaling
            // (You can also hardcode 100 if you know that's your max)
            double maxValue = 0;
            for (int val : state.getData()) {
                if (val > maxValue) maxValue = val;
            }

            int a = state.getHighlightA();
            int b = state.getHighlightB();
            int rStart = state.getRangeStart();
            int rEnd = state.getRangeEnd();

            for (int i = 0; i < n; i++) {
                // 4. Calculate X based on barWidth
                double x = i * barWidth;

                // 5. Calculate Height relative to the pane's height
                double height = (state.get(i) / maxValue) * paneHeight;

                // 6. Calculate Y so bars grow from the bottom
                double y = paneHeight - height;

                Color color = Color.BLACK;

                if (i == a || i == b) {
                    color = Color.RED;
                } else if (rStart != -1 && rEnd != -1 && i >= rStart && i <= rEnd) {
                    color = Color.LIGHTCORAL;
                }

                drawRectangle(x, y, barWidth, height, color, renderPane);
            }
        }
        else if (structureOptionSelected.equals("Trees")) {

        }

        // Update labels...
        lblStepCount.setText("Steps: " + arrayRunner.getIndex() + " / " + arrayRunner.getTotalSteps());
    }

    private void initDemo() {
        int[] demo = {4, 2, 7, 1, 8, 3, 9, 5, 10, 6};
        List<Step<ArrayState>> demoSteps = new ArrayList<>();
        demoSteps.add(new ArrayCompareStep(2, 8));
        demoSteps.add(new SwapStep(2, 8));
        demoSteps.add(new SwapStep(8, 2));
        demoSteps.add(new SetValueStep(9, 30));
        demoSteps.add(new HighlightStep(4, 6));
        demoSteps.add(new ArrayCompareStep(4, 6));
//        arrayRunner = new StepRunner<ArrayState>(demo, demoSteps, ArrayState::new);
        arrayRunner = new StepRunner<ArrayState>(demo, demoSteps, ArrayState::new);
        render();
    }

    private void LoadInsertionSortDemo(int arraySize) {
        int[] data = new Random().ints(arraySize, 1, 101).toArray();
        ArrayState temp = new ArrayState(data);
        InsertionSortGenerator generator = new InsertionSortGenerator();
        List<Step<ArrayState>> steps = generator.generate(temp);
        arrayRunner = new StepRunner<ArrayState>(data, steps, ArrayState::new);
        render();
    }

//    private void loadArrays(int arraySize) {
//        String optionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();
//        if (optionSelected.equals("Array")) {
//            int[] data = new Random().ints(arraySize, 1, 10000).toArray();
//            ArrayState temp = new ArrayState(data);
//            StepGenerator<ArrayState> generator = getSelectedGenerator();
//            List<Step<ArrayState>> steps = generator.generate(temp);
//            arrayRunner = new StepRunner<>(data, steps, ArrayState::new);
//        }
////        else if (optionSelected.equals("Trees")) {
////
////        }
//        render();
//    }
    private void loadStructure(int arraySize) {
        String optionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();
        if (optionSelected.equals("Array")) {
            int[] data = new Random().ints(arraySize, 1, 10000).toArray();
            ArrayState temp = new ArrayState(data);
            StepGenerator<ArrayState> generator = getSelectedArrayGenerator();
            List<Step<ArrayState>> steps = generator.generate(temp);
            arrayRunner = new StepRunner<ArrayState>(data, steps, ArrayState::new);
        }
        else if (optionSelected.equals("Tree")) {
            int[] data = new Random().ints(arraySize, 1, 6).toArray();
            List<Integer> keys = Arrays.stream(data).boxed().toList();
//            BstNode[] tempNodes = new BstNode[data.length];
//            for (int i = 0; i < data.length; i++) {
//                tempNodes[i] = new BstNode(data[i]);
//            }
            StepGenerator<BstState> generator = getSelectedTreeGenerator();
            generator.setKeysToInsert(keys);
            List<Step<BstState>> steps = generator.generate(new BstState(data));
            bstRunner = new StepRunner<BstState>(data, steps, BstState::new);
        }
    //        else if (optionSelected.equals("Trees")) {
    //
    //        }
        render();
    }

    private StepGenerator<ArrayState> getSelectedArrayGenerator() {
        String selected = cmbAlgorithm.getValue();
        String optionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();

        if (selected.equals("Merge Sort")) {
            return new MergeSortGenerator();
        } else if (selected.equals("Bubble Sort")) {
            return new BubbleSortGenerator();
        } else if (selected.equals("Selection Sort")) {
            return new SelectionSortGenerator();
        } else if (selected.equals("Quick Sort")) {
            return new QuickSortGenerator();
        } else if (selected.equals("Heap Sort")) {
            return new HeapSortGenerator();
        } else if (selected.equals("Radix Sort")) {
            return new RadixSortGenerator();
        }

        return new InsertionSortGenerator();
    }

    private StepGenerator<BstState> getSelectedTreeGenerator() {
        String selected = cmbAlgorithm.getValue();

        if (selected.equals("Insert")) {
            return new BstInsertGenerator();
        }

        return new BstInsertGenerator();
    }
}