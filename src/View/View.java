package View;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import Controller.Controller;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class View extends Application implements Observer {
    private Controller controller; // TODO: Instantiate in main

    private static final int CELL_SIZE = 20;
    private static final int WINDOW_SIZE = CELL_SIZE * 36;

    // Testing variables. TODO: Remove


    public static void main(String args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stomachion");

        // Test canvas
        Canvas canvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, CELL_SIZE, CELL_SIZE);

        // Show initial setup
        Group group = new Group();
        group.getChildren().addAll(canvas);
        Scene scene = new Scene(group, WINDOW_SIZE, WINDOW_SIZE);
        stage.setScene(scene);
        stage.show();
    }
}
