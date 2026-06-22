package app.ui;

import algo.*;
import algo.Arrays.*;
import algo.Hash.*;
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
import model.HashState;
import model.HeapState;
import render.PositionedNode;
import render.TreeLayoutHelper;
import steps.*;
import util.StepRunner;
import util.HashUtils;
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
    private StepRunner<HashState> hashRunner;
    private Timeline timeline;
    private Map<String, ArrayList<String>> structureToAlgos;
    private String optionSelected;
    private String algoSelected;
    private List<Integer> currentTreeKeys = new ArrayList<>();




    public void initialize() {
        structureToAlgos = new LinkedHashMap<>();
        structureToAlgos.put("Array", new ArrayList<>(List.of("Insertion Sort", "Merge Sort", "Bubble Sort", "Selection Sort", "Quick Sort", "Heap Sort", "Radix Sort")));
        structureToAlgos.put("Trees", new ArrayList<>(List.of("Create", "Insert", "AVL Insert", "Search", "Delete")));
        structureToAlgos.put("Heap", new ArrayList<>(List.of("Max Heap Insert", "Max Heap Extract", "Min Heap Insert", "Min Heap Extract")));
        structureToAlgos.put("Hash Table", new ArrayList<>(List.of("Create", "Insert", "Search")));
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
            (optionSelected.equals("Heap") && algoSelected.endsWith("Insert")) ||
            (optionSelected.equals("Hash Table") && !algoSelected.equals("Create"));
    inputTXTField.setVisible(needsInput);
    submitTxtField.setVisible(needsInput);
    if (optionSelected.equals("Heap") && algoSelected.endsWith("Insert")) {
        inputTXTField.setPromptText("Enter number to insert");
        submitTxtField.setText("Insert");
    } else if (optionSelected.equals("Hash Table") && !algoSelected.equals("Create")) {
        inputTXTField.setPromptText("Enter number to " + algoSelected);
        submitTxtField.setText(algoSelected);
    }
    onLoad();
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
            boolean needsInput = algoSelected.endsWith("Insert");
            inputTXTField.setVisible(needsInput);
            submitTxtField.setVisible(needsInput);
            if (needsInput) {
                inputTXTField.setPromptText("Enter number to insert");
                submitTxtField.setText("Insert");
            }
        } else if (optionSelected.equals("Hash Table")) {
            boolean needsInput = !algoSelected.equals("Create");
            inputTXTField.setVisible(needsInput);
            submitTxtField.setVisible(needsInput);
            if (needsInput) {
                inputTXTField.setPromptText("Enter number to " + algoSelected);
                submitTxtField.setText(algoSelected);
            }
        } else {
            inputTXTField.setVisible(false);
            submitTxtField.setVisible(false);
        }
        onLoad();
    }

    public void onLoad() {
        System.out.println("Loaded!");
        if (timeline != null) timeline.stop(); // Stop any current playback
        this.lblStatus.setText("Not sorted");
        this.lblNoOps.setText("");
        this.loadStructure(40);
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
                break;
            case "Hash Table":
                if (hashRunner == null) {
                    System.out.println("Load hash table first!");
                    return;
                }
                hashRunner.reset();
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
                break;
            case "Hash Table":
                if (hashRunner == null) {
                    System.out.println("Load hash table first!");
                    return;
                }
                if (hashRunner.hasPrev()) {
                    hashRunner.prev();
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
//                System.out.println(arrayRunner.getCurrentStep().getDescription());
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
//                System.out.println(bstRunner.getCurrentStep().getDescription());
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
                if (isValidSelectedHeap(heapRunner.getState())) {
                    lblNoOps.setText(heapRunner.getState().isMinHeap() ? "Valid min heap!" : "Valid max heap!");
                } else {
                    lblNoOps.setText("Heapifying...");
                }
//                System.out.println(heapRunner.getCurrentStep().getDescription());
                render();
            }
            break;

        case "Hash Table":
            if (hashRunner == null) {
                System.out.println("Load hash table first!");
                return;
            }
            if (hashRunner.hasNext()) {
                hashRunner.next();
                lblNoOps.setText(hashRunner.getCurrentStep().getDescription());
//                System.out.println(hashRunner.getCurrentStep().getDescription());
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
                    (optionSelected.equals("Heap") && heapRunner != null && heapRunner.hasNext()) ||
                    (optionSelected.equals("Hash Table") && hashRunner != null && hashRunner.hasNext());

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
        rectangle.setStroke(Color.web("#d8dee9"));
        rectangle.setArcWidth(4);
        rectangle.setArcHeight(4);
        drawingPane.getChildren().add(rectangle);
    }

    private void render() {
//        System.out.println(optionSelected);

        renderPane.getChildren().clear();

        if (optionSelected.equals("Array")) {
            lblStatus.setVisible(true);
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

                Color color = Color.web("#2563eb");

                if (i == a || i == b) {
                    color = Color.web("#ef4444");
                } else if (rStart != -1 && rEnd != -1 && i >= rStart && i <= rEnd) {
                    color = Color.web("#f59e0b");
                }

                drawRectangle(x, y, barWidth, height, color, renderPane);
            }

            lblStepCount.setText("Steps: " + arrayRunner.getIndex() + " / " + arrayRunner.getTotalSteps());
        }
        else if (optionSelected.equals("Trees")) {
            lblStatus.setVisible(false);
            renderBst(bstRunner.getState());
        }
        else if (optionSelected.equals("Heap")) {
            lblStatus.setVisible(false);
            renderHeap(heapRunner.getState());
        }
        else if (optionSelected.equals("Hash Table")) {
            lblStatus.setVisible(false);
            renderHash(hashRunner.getState());
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
            lblStatus.setText(state.isMinHeap() ? "Valid Min Heap" : "Valid Max Heap");
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
                Line line = new Line(parentPos[0], parentPos[1], childPos[0], childPos[1]);
                line.setStroke(Color.web("#94a3b8"));
                line.setStrokeWidth(2);
                renderPane.getChildren().add(line);
            }
            if (right < size) {
                double[] childPos = positions.get(right);
                Line line = new Line(parentPos[0], parentPos[1], childPos[0], childPos[1]);
                line.setStroke(Color.web("#94a3b8"));
                line.setStrokeWidth(2);
                renderPane.getChildren().add(line);
            }
        }

        for (int i = 0; i < size; i++) {
            double[] pos = positions.get(i);
            Color color = (i == a || i == b) ? Color.web("#ef4444") : Color.web("#1e293b");
            Circle circle = new Circle(pos[0], pos[1], radius, color);
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(2);
            renderPane.getChildren().add(circle);

            Label label = new Label(Integer.toString(state.get(i)));
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-weight: 700;");
            label.relocate(pos[0] - 10, pos[1] - 9);
            renderPane.getChildren().add(label);
        }

        lblStepCount.setText("Steps: " + heapRunner.getIndex() + " / " + heapRunner.getTotalSteps());
        lblComparisons.setText("Comparisons: " + state.getCounters().getComparisons());
        lblSwaps.setText("Swaps: " + state.getCounters().getSwaps());
        lblWrites.setText("Writes: " + state.getCounters().getWrites());
        lblStatus.setText(isValidSelectedHeap(state)
                ? (state.isMinHeap() ? "Valid Min Heap" : "Valid Max Heap")
                : (state.isMinHeap() ? "Invalid Min Heap" : "Invalid Max Heap"));
    }

    private void renderHash(HashState state) {
        renderPane.getChildren().clear();

        double paneWidth = renderPane.getWidth() > 0 ? renderPane.getWidth() : 700;
        double slotWidth = Math.min(80, Math.max(48, (paneWidth - 40) / state.getCapacity()));
        double slotHeight = 64;
        double gap = 8;
        double startX = 20;
        double startY = 120;
        int highlight = state.getHighlightIndex();

        for (int i = 0; i < state.getCapacity(); i++) {
            double x = startX + i * (slotWidth + gap);
            double y = startY;

            if (x + slotWidth > paneWidth - 10) {
                int columns = Math.max(1, (int) ((paneWidth - 40) / (slotWidth + gap)));
                int row = i / columns;
                int col = i % columns;
                x = startX + col * (slotWidth + gap);
                y = startY + row * 95;
            }

            Color fill = i == highlight ? Color.web("#fed7aa") : Color.WHITE;
            Rectangle slot = new Rectangle(x, y, slotWidth, slotHeight);
            slot.setFill(fill);
            slot.setStroke(i == highlight ? Color.web("#f97316") : Color.web("#cbd5e1"));
            slot.setStrokeWidth(i == highlight ? 2 : 1);
            slot.setArcWidth(8);
            slot.setArcHeight(8);
            renderPane.getChildren().add(slot);

            Label indexLabel = new Label(Integer.toString(i));
            indexLabel.setTextFill(Color.web("#64748b"));
            indexLabel.setStyle("-fx-font-weight: 700;");
            indexLabel.relocate(x + 4, y - 24);
            renderPane.getChildren().add(indexLabel);

            Integer value = state.get(i);
            Label valueLabel = new Label(value == null ? "empty" : value.toString());
            valueLabel.setTextFill(value == null ? Color.web("#94a3b8") : Color.web("#172033"));
            valueLabel.setStyle("-fx-font-weight: 700;");
            valueLabel.relocate(x + 10, y + 22);
            renderPane.getChildren().add(valueLabel);

            if (value != null) {
                Label hashLabel = new Label("h=" + state.hash(value));
                hashLabel.setTextFill(Color.web("#475569"));
                hashLabel.relocate(x + 8, y + slotHeight + 4);
                renderPane.getChildren().add(hashLabel);
            }
        }

        lblStepCount.setText("Steps: " + hashRunner.getIndex() + " / " + hashRunner.getTotalSteps());
        lblComparisons.setText("Comparisons: " + state.getCounters().getComparisons());
        lblSwaps.setText("Swaps: 0");
        lblWrites.setText("Writes: " + state.getCounters().getWrites());
        String status = HashUtils.isValidLinearProbingTable(state.getTable()) ? "Valid Linear Probing Table" : "Invalid Linear Probing Table";
        lblStatus.setText(status + " | Load Factor: " + String.format("%.2f", state.getLoadFactor()));
    }

    private void renderBst(BstState state) {
//        System.out.println("RENDERING BST!!!");
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
                line.setStroke(Color.web("#94a3b8"));
                line.setStrokeWidth(2);
                renderPane.getChildren().add(line);
            }
            if (current.getBstNodeRight() != null) {
                PositionedNode rightChild = helper.findPosition(current.getBstNodeRight(), positionedNodes);
                Line line = new Line(node.getX(), node.getY(), rightChild.getX(), rightChild.getY());
                line.setStroke(Color.web("#94a3b8"));
                line.setStrokeWidth(2);
                renderPane.getChildren().add(line);
            }
        }

        int radius = 20;
        Color defaultColor = Color.web("#1e293b");
        Color highlightColor = Color.web("#ef4444");
        for (PositionedNode node : positionedNodes) {
            Circle circle = new Circle(node.getX(), node.getY(), radius, defaultColor);
            if (state.getHighlightKey() != null && node.getNode().getKey() == state.getHighlightKey()) {
                circle.setFill(highlightColor);
            }
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(2);
            renderPane.getChildren().add(circle);
            Label label = new Label(Integer.toString(node.getNode().getKey()));
            label.relocate(node.getX() - 2, node.getY() - 2);
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-weight: 700;");
            renderPane.getChildren().add(label);
//            System.out.println("Rendered circle for "+ node.getNode().getKey() + " at (x,y): " + circle.getCenterX() + ", " + circle.getCenterY());
//            System.out.println("Rendered circle's node's (x,y): " + node.getX() + ", " + node.getY());
//            System.out.println("Panel width: " + renderPane.getWidth());
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
        if (optionSelected.equals("Array")) {
            int[] data = new Random().ints(arraySize, 1, 10000).toArray();
            ArrayState temp = new ArrayState(data);
            StepGenerator<ArrayState> generator = getSelectedArrayGenerator();
            List<Step<ArrayState>> steps = generator.generate(temp);
            arrayRunner = new StepRunner<ArrayState>(data, steps, ArrayState::new);
        }
        else if (optionSelected.equals("Trees")) {
            int treeCapacity = 20;
            int[] data = new Random().ints(1, 100).distinct().limit(treeCapacity).toArray();
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
            boolean minHeap = isSelectedMinHeap();
            HeapState temp = new HeapState(data, minHeap);
            StepGenerator<HeapState> generator = getSelectedHeapGenerator();
            if (algoSelected.endsWith("Insert")) {
                generator.setKeyToInsert(new Random().nextInt(99) + 1);
            }
            List<Step<HeapState>> steps = generator.generate(temp);
            heapRunner = new StepRunner<HeapState>(data, steps, initialData -> new HeapState(initialData, minHeap));
        }
        else if (optionSelected.equals("Hash Table")) {
            int capacity = 13;
            List<Integer> keys = new Random().ints(1, 100).distinct().limit(capacity - 2).boxed().toList();
            HashInitializeGenerator generator = new HashInitializeGenerator();
            generator.setKeysToInsert(keys);
            List<Step<HashState>> steps = generator.generate(new HashState(capacity));
            hashRunner = new StepRunner<HashState>(new int[]{capacity}, steps, data -> new HashState(data[0]));
        }
        render();
    }

    private StepGenerator<HeapState> getSelectedHeapGenerator() {
        String selected = cmbAlgorithm.getValue();
        boolean minHeap = isSelectedMinHeap();

        if (selected.endsWith("Extract")) {
            return new HeapExtractGenerator(minHeap);
        }
        return new HeapInsertGenerator(minHeap);
    }

    private boolean isSelectedMinHeap() {
        String selected = cmbAlgorithm.getValue();
        return selected != null && selected.startsWith("Min");
    }

    private boolean isValidSelectedHeap(HeapState state) {
        if (state.isMinHeap()) {
            return HeapUtils.isValidMinHeap(state.getData(), state.getSize());
        }
        return HeapUtils.isValidMaxHeap(state.getData(), state.getSize());
    }

    private StepGenerator<HashState> getSelectedHashGenerator() {
        String selected = cmbAlgorithm.getValue();

        return switch (selected) {
            case "Create" -> new HashInitializeGenerator();
            case "Search" -> new HashSearchGenerator();
            case "Insert" -> new HashInsertGenerator();
            default -> new HashInsertGenerator();
        };
    }

    private StepGenerator<ArrayState> getSelectedArrayGenerator() {
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
                boolean minHeap = isSelectedMinHeap();
                HeapInsertGenerator generator = new HeapInsertGenerator(minHeap);
                generator.setKeyToInsert(key);
                HeapState current = heapRunner == null ? new HeapState(new int[]{}, minHeap) : heapRunner.getState();
                int[] baseData = Arrays.copyOf(current.getData(), current.getSize());
                List<Step<HeapState>> steps = generator.generate(current);
                heapRunner = new StepRunner<HeapState>(baseData, steps, initialData -> new HeapState(initialData, minHeap));
                render();
                return;
            }

            if (optionSelected.equals("Hash Table")) {
                HashState current = hashRunner == null ? new HashState(11) : hashRunner.getState();
                Integer[] tableBefore = Arrays.copyOf(current.getTable(), current.getCapacity());
                StepGenerator<HashState> generator = getSelectedHashGenerator();
                generator.setKeyToInsert(key);
                List<Step<HashState>> steps = generator.generate(current);
                hashRunner = new StepRunner<HashState>(new int[]{current.getCapacity()}, steps, data -> new HashState(tableBefore));
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
//            System.out.println("Please enter a valid number.");
            lblNoOps.setText("Error: Please enter a valid number.");
        }
    }
}
