package app.ui;

import algo.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.ArrayState;
import steps.*;
import util.StepRunner;

import java.sql.SQLOutput;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    private StepRunner runner;
    private Timeline timeline;

    public void initialize() {
        cmbAlgorithm.getItems().addAll("Insertion Sort", "Merge Sort", "Bubble Sort", "Selection Sort", "Quick Sort", "Heap Sort", "Radix Sort");

        cmbAlgorithm.getSelectionModel().selectFirst();
    }

    public void onLoad() {
        System.out.println("Loaded!");
//        this.initDemo();
//        this.LoadInsertionSortDemo(5);
        this.lblStatus.setText("Not sorted");
        this.LoadSelectedSortDemo(100);
    }

    public void onReset() {
        if (runner == null) {
            System.out.println("Load array first!");
            return;
        }
        runner.reset();
        render();
    }

    public void onPrev() {
        if (runner == null) {
            System.out.println("Load array first!");
            return;
        }
        if (runner.hasPrev()) {
            runner.prev();
            render();
            if (Objects.equals(lblStatus.getText(), "Sorted")) {
                lblStatus.setText("Not sorted ");
            }
        }
    }

    public void onNext() {
        if (runner == null) {
            System.out.println("Load array first!");
            return;
        }
        if (runner.hasNext()) {
            runner.next();
            if (runner.getCurrentStep() instanceof NoOpStep) {
                lblNoOps.setText(runner.getCurrentStep().getDescription());
            }
            else { lblNoOps.setText(""); }
            System.out.println(runner.getCurrentStep().getDescription());
            render();
            if (!runner.hasNext()) {
                if (StepRunner.isSorted(runner.getState().getData())) {
                    lblStatus.setText("Sorted");
                }
            }
        }
    }

    public void onPlay() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
//                if (runner.hasNext()) {
//                    runner.next();
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
    ArrayState state = runner.getState();
    renderPane.getChildren().clear();

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

    // Update labels...
    lblStepCount.setText("Steps: " + runner.getIndex() + " / " + runner.getTotalSteps());
}

    private void initDemo() {
        int[] demo = {4, 2, 7, 1, 8, 3, 9, 5, 10, 6};
        List<Step> demoSteps = new ArrayList<>();
        demoSteps.add(new CompareStep(2, 8));
        demoSteps.add(new SwapStep(2, 8));
        demoSteps.add(new SwapStep(8, 2));
        demoSteps.add(new SetValueStep(9, 30));
        demoSteps.add(new HighlightStep(4, 6));
        demoSteps.add(new CompareStep(4, 6));
        runner = new StepRunner(demo, demoSteps);
        render();
    }

    private void LoadInsertionSortDemo(int arraySize) {
        int[] data = new Random().ints(arraySize, 1, 101).toArray();
        ArrayState temp = new ArrayState(data);
        InsertionSortGenerator generator = new InsertionSortGenerator();
        List<Step> steps = generator.generate(temp);
        runner = new StepRunner(data, steps);
        render();
    }

    private void LoadSelectedSortDemo(int arraySize) {
        int[] data = new Random().ints(arraySize, 1, 10000).toArray();
        ArrayState temp = new ArrayState(data);
        StepGenerator generator = getSelectedGenerator();
        List<Step> steps = generator.generate(temp);
        runner = new StepRunner(data, steps);
        render();
    }

    private StepGenerator getSelectedGenerator() {
        String selected = cmbAlgorithm.getValue();

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
}