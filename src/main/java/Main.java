import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.MineSweeper;

import java.util.Scanner;

/**
 * Main osztály, a javafx applikáció elindítására.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        Scene scene = new Scene(root,600,400);
        primaryStage.setTitle("Board Games");
        scene.getStylesheets().add("mainscreen.css");
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    /**
     * A program main metódusa.
     *
     * Egyetlen feladata, az applikáció elindítása.
     *
     * @param args parancssori argumentumok.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
