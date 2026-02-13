package app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

    public void onLoad() {
        System.out.println("Loaded!");;
    }
    public void onReset() {
        System.out.println("Reset!");
    }
    public void onPrev() {
        System.out.println("Previous!");
    }
    public void onNext() {
        System.out.println("Next!");
    }
    public void onPlay() {
        System.out.println("Played!");
    }
    public void onPause() {
        System.out.println("Paused!");
    }
}
