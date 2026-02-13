package app;

import app.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage mainStage;
    private static Scene scene;
    private static MainController mainController;

    @Override
    public void start(Stage stage) {
        try {
            mainStage = stage;

            FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = rootLoader.load();
            mainController = rootLoader.getController();

            // Roots

            // Controllers

            // Scenes
            scene = new Scene(root);

//            String css = this.getClass().getResource("/styles.css").toExternalForm();
//            scene.getStylesheets().add(css); add whenever you create the css

            mainStage.setScene(scene);
            mainStage.setTitle("Algorithm Visualizer");
            mainStage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
