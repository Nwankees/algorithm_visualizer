package app.ui;

import algo.*;
import algo.Arrays.*;
import algo.Heaps.*;
import algo.Trees.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.ArrayState;
import model.BstNode;
import model.BstState;
import model.HeapState;
import render.PositionedNode;
import render.TreeLayoutHelper;
import steps.*;
import util.StepRunner;
import util.HeapUtils;

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
    @FXML
    private TextField inputTXTField;
    @FXML
    private Button submitTxtField;

    private StepRunner<ArrayState> arrayRunner;
    private StepRunner<BstState> bstRunner;
    private StepRunner<HeapState> heapRunner;
    private Timeline timeline;
    private Map<String, ArrayList<String>> structureToAlgos;
    private String optionSelected;
    private String algoSelected;
    private List<Integer> currentTreeKeys = new ArrayList<>();




    public void initialize() {
        structureToAlgos = new LinkedHashMap<>();
        structureToAlgos.put("Array", new ArrayList<>(List.of("Insertion Sort", "Merge Sort", "Bubble Sort", "Selection Sort", "Quick Sort", "Heap Sort", "Radix Sort")));
        structureToAlgos.put("Trees", new ArrayList<>(List.of("Create", "Insert", "AVL Insert", "Search", "Delete")));
        structureToAlgos.put("Heap", new ArrayList<>(List.of("Heap Insert", "Heap Extract")));
        cmbAlgorithm.getItems().addAll(structureToAlgos.get("Array"));
        cmbViewMode.getItems().addAll(structureToAlgos.keySet());

        cmbAlgorithm.getSelectionModel().selectFirst();
        cmbViewMode.getSelectionModel().selectFirst();
        optionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();
        algoSelected = this.cmbAlgorithm.getSelectionModel().getSelectedItem();
        inputTXTField.setVisible(false);
        submitTxtField.setVisible(false);
    }

public void changeOptionSelected() {
    cmbAlgorithm.getItems().clear();
    cmbAlgorithm.getItems().addAll(structureToAlgos.get(cmbViewMode.getSelectionModel().getSelectedItem()));
    cmbAlgorithm.getSelectionModel().selectFirst();
    optionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();

    // Toggle visibility based on mode
    algoSelected = cmbAlgorithm.getSelectionModel().getSelectedItem();
    boolean needsInput = (optionSelected.equals("Trees") && !algoSelected.equals("Create")) ||
            (optionSelected.equals("Heap") && algoSelected.equals("Heap Insert"));
    inputTXTField.setVisible(needsInput);
    submitTxtField.setVisible(needsInput);
    if (optionSelected.equals("Heap") && algoSelected.equals("Heap Insert")) {
        inputTXTField.setPromptText("Enter number to insert");
        submitTxtField.setText("Insert");
    }
}

    public void changeAlgoSelected() {
        algoSelected = cmbAlgorithm.getSelectionModel().getSelectedItem();

        if (optionSelected.equals("Trees")) {
            boolean needsInput = !algoSelected.equals("Create");
            inputTXTField.setVisible(needsInput);
            submitTxtField.setVisible(needsInput);
            if(needsInput) {
                inputTXTField.setPromptText("Enter number to " + algoSelected);
                submitTxtField.setText(algoSelected);
            }
        } else if (optionSelected.equals("Heap")) {
            boolean needsInput = algoSelected.equals("Heap Insert");
            inputTXTField.setVisible(needsInput);
            submitTxtField.setVisible(needsInput);
            if (needsInput) {
                inputTXTField.setPromptText("Enter number to insert");
                submitTxtField.setText("Insert");
            }
        } else {
            inputTXTField.setVisible(false);
            submitTxtField.setVisible(false);
        }
    }

    public void onLoad() {
        System.out.println("Loaded!");
        if (timeline != null) timeline.stop(); // Stop any current playback
        this.lblStatus.setText("Not sorted");
        this.loadStructure(7);
    }

    public void onReset() {
        if (timeline != null) timeline.stop(); // Stop playback on reset
        switch (optionSelected) {
            case "Array":
                if (arrayRunner == null) {
                    System.out.println("Load array first!");
                    return;
                }
                arrayRunner.reset();
                break;
            case "Trees":
                if (bstRunner == null) {
                    System.out.println("Load tree first!");
                    return;
                }
                bstRunner.reset();
                break;
            case "Heap":
                if (heapRunner == null) {
                    System.out.println("Load heap first!");
                    return;
                }
                heapRunner.reset();
        }

        render();
    }

    public void onPrev() {
        switch (optionSelected) {
            case "Array":
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
                break;
            case "Trees":
                if (bstRunner == null) {
                    System.out.println("Load tree first!");
                    return;
                }
                if (bstRunner.hasPrev()) {
                    bstRunner.prev();
                    render();
                }
                break;
            case "Heap":
                if (heapRunner == null) {
                    System.out.println("Load heap first!");
                    return;
                }
                if (heapRunner.hasPrev()) {
                    heapRunner.prev();
                    render();
                }
        }

    }

public void onNext() {
    switch (optionSelected) {
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
                if (StepRunner.isValidBST(bstRunner.getState().getRoot())) {
                    lblNoOps.setText("Valid BST!");
                }
                System.out.println(bstRunner.getCurrentStep().getDescription());
                render();
            }
            break;

        case "Heap":
            if (heapRunner == null) {
                System.out.println("Load heap first!");
                return;
            }
            if (heapRunner.hasNext()) {
                heapRunner.next();
                if (HeapUtils.isValidMaxHeap(heapRunner.getState().getData(), heapRunner.getState().getSize())) {
                    lblNoOps.setText("Valid max heap!");
                } else {
                    lblNoOps.setText("Heapifying...");
                }
                System.out.println(heapRunner.getCurrentStep().getDescription());
                render();
            }
            break;
    }
}

    public void onPlay() {
        // If a timeline is already running, stop it before starting a new one
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            // Check if there is actually a next step before calling onNext
            boolean hasMore = (optionSelected.equals("Array") && arrayRunner != null && arrayRunner.hasNext()) ||
                    (optionSelected.equals("Trees") && bstRunner != null && bstRunner.hasNext()) ||
                    (optionSelected.equals("Heap") && heapRunner != null && heapRunner.hasNext());

            if (hasMore) {
                this.onNext();
            } else {
                timeline.stop();
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void onPause() {
        if (timeline != null) {
            timeline.stop();
        }
        System.out.println("Paused!");
    }

    public void drawRectangle(double x, double y, double width, double height, Color color, Pane drawingPane) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(color);
        drawingPane.getChildren().add(rectangle);
    }

    private void render() {
        System.out.println(optionSelected);

        renderPane.getChildren().clear();

        if (optionSelected.equals("Array")) {
            ArrayState state = arrayRunner.getState();
            double paneWidth = renderPane.getWidth();
            double paneHeight = renderPane.getHeight();
            int n = state.getData().length;

            double barWidth = paneWidth / n;

            double maxValue = 0;
            for (int val : state.getData()) {
                if (val > maxValue) maxValue = val;
            }

            int a = state.getHighlightA();
            int b = state.getHighlightB();
            int rStart = state.getRangeStart();
            int rEnd = state.getRangeEnd();

            for (int i = 0; i < n; i++) {
                double x = i * barWidth;
                double height = (state.get(i) / maxValue) * paneHeight;
                double y = paneHeight - height;

                Color color = Color.BLACK;

                if (i == a || i == b) {
                    color = Color.RED;
                } else if (rStart != -1 && rEnd != -1 && i >= rStart && i <= rEnd) {
                    color = Color.LIGHTCORAL;
                }

                drawRectangle(x, y, barWidth, height, color, renderPane);
            }

            lblStepCount.setText("Steps: " + arrayRunner.getIndex() + " / " + arrayRunner.getTotalSteps());
        }
        else if (optionSelected.equals("Trees")) {
            renderBst(bstRunner.getState());
        }
        else if (optionSelected.equals("Heap")) {
            renderHeap(heapRunner.getState());
        }
    }

    private void renderHeap(HeapState state) {
        renderPane.getChildren().clear();

        int size = state.getSize();
        if (size == 0) {
            lblStepCount.setText("Steps: " + heapRunner.getIndex() + " / " + heapRunner.getTotalSteps());
            lblComparisons.setText("Comparisons: " + state.getCounters().getComparisons());
            lblSwaps.setText("Swaps: " + state.getCounters().getSwaps());
            lblWrites.setText("Writes: " + state.getCounters().getWrites());
            lblStatus.setText("Valid Max Heap");
            return;
        }

        double paneWidth = renderPane.getWidth() > 0 ? renderPane.getWidth() : 700;
        int radius = 20;
        double verticalGap = 85;
        int a = state.getHighlightA();
        int b = state.getHighlightB();

        Map<Integer, double[]> positions = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int level = (int) (Math.log(i + 1) / Math.log(2));
            int firstIndexAtLevel = (int) Math.pow(2, level) - 1;
            int positionInLevel = i - firstIndexAtLevel;
            int nodesInLevel = (int) Math.pow(2, level);

            double x = paneWidth * (positionInLevel + 1) / (nodesInLevel + 1);
            double y = 55 + level * verticalGap;
            positions.put(i, new double[]{x, y});
        }

        for (int i = 0; i < size; i++) {
            int left = state.left(i);
            int right = state.right(i);
            double[] parentPos = positions.get(i);

            if (left < size) {
                double[] childPos = positions.get(left);
                renderPane.getChildren().add(new Line(parentPos[0], parentPos[1], childPos[0], childPos[1]));
            }
            if (right < size) {
                double[] childPos = positions.get(right);
                renderPane.getChildren().add(new Line(parentPos[0], parentPos[1], childPos[0], childPos[1]));
            }
        }

        for (int i = 0; i < size; i++) {
            double[] pos = positions.get(i);
            Color color = (i == a || i == b) ? Color.RED : Color.BLACK;
            Circle circle = new Circle(pos[0], pos[1], radius, color);
            renderPane.getChildren().add(circle);

            Label label = new Label(Integer.toString(state.get(i)));
            label.setTextFill(Color.WHITE);
            label.relocate(pos[0] - 10, pos[1] - 9);
            renderPane.getChildren().add(label);
        }

        lblStepCount.setText("Steps: " + heapRunner.getIndex() + " / " + heapRunner.getTotalSteps());
        lblComparisons.setText("Comparisons: " + state.getCounters().getComparisons());
        lblSwaps.setText("Swaps: " + state.getCounters().getSwaps());
        lblWrites.setText("Writes: " + state.getCounters().getWrites());
        lblStatus.setText(HeapUtils.isValidMaxHeap(state.getData(), state.getSize()) ? "Valid Max Heap" : "Invalid Max Heap");
    }

    private void renderBst(BstState state) {
        System.out.println("RENDERING BST!!!");
        renderPane.getChildren().clear();

        if (state.getRoot() == null) {
            System.out.println("NULL");
            return;
        }
        TreeLayoutHelper helper = new TreeLayoutHelper();
        List<PositionedNode> positionedNodes = helper.computeLayout(state.getRoot(), renderPane.getWidth());

        for (PositionedNode node : positionedNodes) {
            BstNode current = node.getNode();

            if (current.getBstNodeLeft() != null) {
                PositionedNode leftChild = helper.findPosition(current.getBstNodeLeft(), positionedNodes);
                Line line = new Line(node.getX(), node.getY(), leftChild.getX(), leftChild.getY());
                renderPane.getChildren().add(line);
            }
            if (current.getBstNodeRight() != null) {
                PositionedNode rightChild = helper.findPosition(current.getBstNodeRight(), positionedNodes);
                Line line = new Line(node.getX(), node.getY(), rightChild.getX(), rightChild.getY());
                renderPane.getChildren().add(line);
            }
        }

        int radius = 20;
        Color defaultColor = Color.BLACK;
        Color highlightColor = Color.RED;
        for (PositionedNode node : positionedNodes) {
            Circle circle = new Circle(node.getX(), node.getY(), radius, defaultColor);
            if (state.getHighlightKey() != null && node.getNode().getKey() == state.getHighlightKey()) {
                circle.setFill(highlightColor);
            }
            renderPane.getChildren().add(circle);
//            Label label = new Label(Integer.toString(node.getNode().getKey()));
            int bf = util.Trees.AvlUtils.getBalance(node.getNode());
            Label label = new Label(node.getNode().getKey() + "(" + bf + ")");
            label.relocate(node.getX(), node.getY());
            label.setTextFill(Color.WHITE);
            renderPane.getChildren().add(label);
            System.out.println("Rendered circle for "+ node.getNode().getKey() + " at (x,y): " + circle.getCenterX() + ", " + circle.getCenterY());
            System.out.println("Rendered circle's node's (x,y): " + node.getX() + ", " + node.getY());
            System.out.println("Panel width: " + renderPane.getWidth());
        }

        lblStepCount.setText("Steps: " + bstRunner.getIndex() + " / " + bstRunner.getTotalSteps());
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

    private void loadStructure(int arraySize) {
//        String optionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();
        if (optionSelected.equals("Array")) {
            int[] data = new Random().ints(arraySize, 1, 10000).toArray();
            ArrayState temp = new ArrayState(data);
            StepGenerator<ArrayState> generator = getSelectedArrayGenerator();
            List<Step<ArrayState>> steps = generator.generate(temp);
            arrayRunner = new StepRunner<ArrayState>(data, steps, ArrayState::new);
        }
        else if (optionSelected.equals("Trees")) {
            int[] data = new Random().ints(1, 100).distinct().limit(arraySize).toArray();
            currentTreeKeys = new ArrayList<>(); // Reset history
            for(int d : data) currentTreeKeys.add(d); // Seed with initial data


            BstInitializeGenerator initGenerator = new BstInitializeGenerator();
            initGenerator.setKeysToInsert(currentTreeKeys);
            List<Step<BstState>> initSteps = initGenerator.generate(new BstState(data));

            bstRunner = new StepRunner<BstState>(data, initSteps, BstState::new);

            if (algoSelected.equals("Insert") || algoSelected.equals("Search") || algoSelected.equals("Delete")) {
                while (bstRunner.hasNext()) bstRunner.next();
            } else {
                if (bstRunner.hasNext()) bstRunner.next();
            }
        }
        else if (optionSelected.equals("Heap")) {
            int[] data = new Random().ints(1, 100).distinct().limit(arraySize).toArray();
            HeapState temp = new HeapState(data);
            StepGenerator<HeapState> generator = getSelectedHeapGenerator();
            if (algoSelected.equals("Heap Insert")) {
                generator.setKeyToInsert(new Random().nextInt(99) + 1);
            }
            List<Step<HeapState>> steps = generator.generate(temp);
            heapRunner = new StepRunner<HeapState>(data, steps, HeapState::new);
        }
        render();
    }

    private StepGenerator<HeapState> getSelectedHeapGenerator() {
        String selected = cmbAlgorithm.getValue();

        if (selected.equals("Heap Extract")) {
            return new HeapExtractGenerator();
        }
        return new HeapInsertGenerator();
    }

    private StepGenerator<ArrayState> getSelectedArrayGenerator() {
        String selected = cmbAlgorithm.getValue();
//        String optionSelected = this.cmbViewMode.getSelectionModel().getSelectedItem();

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
    inputTXTField.setVisible(true);
    submitTxtField.setVisible(true);
    inputTXTField.setPromptText("Enter number to " + selected);
    submitTxtField.setText(selected);

    return switch (selected) {
        case "Create" -> new BstInitializeGenerator();
        case "Insert" -> new BstInsertGenerator();
        case "AVL Insert" -> new AvlInsertGenerator();
        case "Search" -> new BstSearchGenerator();
        case "Delete" -> new BstDeleteGenerator();
        default -> new BstInsertGenerator();
    };
}

    private String getOptionSelected() {
        return this.cmbViewMode.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void onInsertSubmit() {
        int key = Integer.parseInt(inputTXTField.getText());

        BstInsertGenerator gen = new BstInsertGenerator();
        gen.setKeyToInsert(key);
        List<Step<BstState>> path = gen.generate(bstRunner.getState());

        currentTreeKeys.add(key);

        final List<Integer> historyAtThisPoint = new ArrayList<>(currentTreeKeys);

        bstRunner = new StepRunner<>(new int[]{key}, path, data -> {
            BstState newState = new BstState(data);
            List<Integer> previousHistory = historyAtThisPoint.subList(0, historyAtThisPoint.size() - 1);
            BstInitializeGenerator init = new BstInitializeGenerator();
            init.setKeysToInsert(previousHistory);
            List<Step<BstState>> setupSteps = init.generate(newState);
            for(Step<BstState> s : setupSteps) s.apply(newState);

            return newState;
        });

        render();
    }

    @FXML
    public void onOperationSubmit() {
        try {
            int key = Integer.parseInt(inputTXTField.getText());
            if (optionSelected.equals("Heap")) {
                HeapInsertGenerator generator = new HeapInsertGenerator();
                generator.setKeyToInsert(key);
                HeapState current = heapRunner == null ? new HeapState(10) : heapRunner.getState();
                int[] baseData = Arrays.copyOf(current.getData(), current.getSize());
                List<Step<HeapState>> steps = generator.generate(current);
                heapRunner = new StepRunner<HeapState>(baseData, steps, HeapState::new);
                render();
                return;
            }

            final List<Integer> historyBefore = new ArrayList<>(currentTreeKeys);

            StepGenerator<BstState> generator = getSelectedTreeGenerator();
            generator.setKeyToInsert(key);

            List<Step<BstState>> steps = generator.generate(bstRunner.getState());

            if (algoSelected.equals("Insert")) {
                currentTreeKeys.add(key);
            } else if (algoSelected.equals("Delete")) {
                currentTreeKeys.remove(Integer.valueOf(key));
            }

            bstRunner = new StepRunner<>(new int[]{key}, steps, dummyData -> {
                BstState newState = new BstState(dummyData);
                BstInitializeGenerator rebuilder = new BstInitializeGenerator();

                rebuilder.setKeysToInsert(historyBefore);
                for (Step<BstState> s : rebuilder.generate(newState)) {
                    s.apply(newState);
                }
                return newState;
            });

            render();
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}
