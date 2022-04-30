package View;

import Utilities.Piece;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

import Controller.Controller;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

public class View extends Application implements Observer {
    private Controller controller; // TODO: Instantiate in main
    private Canvas mainCanvas, selectionCanvas;
    private double initialCoordsX, initialCoordsY;

    private static final double CELL_SIZE = 25;
    private static final double WINDOW_SIZE = CELL_SIZE * 36;
    private static final double BOARD_SIZE = CELL_SIZE * 12;

    private static final double LINE_WIDTH = 2;
    private static final Color LINE_COLOR = Color.rgb(36, 36, 36);
    private static final Color BG_COLOR = Color.grayRgb(230);
    private static final Color FG_COLOR = Color.WHITE;

    // TODO: Delete these variables
    Piece piece;

    public static void main(String args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object piecesRaw) {
        assert piecesRaw instanceof HashSet[];

        HashSet<Integer>[] pieces = (HashSet<Integer>[]) piecesRaw;

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stomachion");

        controller = new Controller();

        // Initialize all canvasses
        GraphicsContext gc;

        mainCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = mainCanvas.getGraphicsContext2D();
        gc.setStroke(LINE_COLOR);
        gc.setLineWidth(LINE_WIDTH);
        gc.setLineCap(StrokeLineCap.ROUND);
        mainCanvas.setOnMousePressed(new MousePressHandler());
        mainCanvas.setOnMouseMoved(new MouseMoveHandler());

        selectionCanvas = new Canvas(BOARD_SIZE, BOARD_SIZE);
        gc = selectionCanvas.getGraphicsContext2D();
        gc.setLineWidth(LINE_WIDTH);
        selectionCanvas.setLayoutX(WINDOW_SIZE);
        selectionCanvas.setLayoutY(WINDOW_SIZE);
        selectionCanvas.setOnMouseReleased(new MouseReleaseHandler());
        selectionCanvas.setOnKeyPressed(new KeyPressHandler());
        gc.save();

        Canvas gridCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = gridCanvas.getGraphicsContext2D();
        gc.setFill(FG_COLOR);
        gc.fillRect(BOARD_SIZE, BOARD_SIZE, BOARD_SIZE, BOARD_SIZE);
        gc.setFill(LINE_COLOR);
        for (int i = 0; i < 37; i++) {
            for (int j = 0; j < 37; j++) {
                gc.fillOval(i * CELL_SIZE - LINE_WIDTH / 2, j * CELL_SIZE - LINE_WIDTH / 2, LINE_WIDTH, LINE_WIDTH);
            }
        }
        gc.setStroke(LINE_COLOR);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeRect(BOARD_SIZE, BOARD_SIZE, BOARD_SIZE, BOARD_SIZE);

        // Test Canvas
        piece = new Piece(2);
        piece.addToGlobalOffset(6, 4);
        drawPiece(piece, mainCanvas);

        // Show initial setup
        Group group = new Group();
        group.getChildren().addAll(gridCanvas, mainCanvas, selectionCanvas);
        Scene scene = new Scene(group, WINDOW_SIZE, WINDOW_SIZE);
        scene.setFill(BG_COLOR);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void drawPiece(Piece piece, Canvas canvas) {
        double[][] allCoords = piece.getAllCoords(CELL_SIZE);
        double[] xCoords = allCoords[0];
        double[] yCoords = allCoords[1];

        // Fill piece
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image texture = new Image("/Textures/BlueCrimsonYellow.png");
        gc.setFill(new ImagePattern(texture, xCoords[0], yCoords[0], CELL_SIZE * 7, CELL_SIZE * 7, false));
        gc.fillPolygon(xCoords, yCoords, xCoords.length);

        // Draw piece border
        for (int i = 0; i < xCoords.length; i++) {
            double endX = i == xCoords.length - 1 ? xCoords[0] : xCoords[i + 1];
            double endY = i == yCoords.length - 1 ? yCoords[0] : yCoords[i + 1];

            gc.strokeLine(xCoords[i], yCoords[i], endX, endY);
        }
    }

    // TODO: Implement
    private class MouseMoveHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            double gridX = mouseEvent.getSceneX() / CELL_SIZE;
            double gridY = mouseEvent.getSceneY() / CELL_SIZE;

            if (controller.hasPieceSelected()) {
                controller.checkPlacement(gridX, gridY);
            }
            else {
                controller.highlight(gridX, gridY);
            }
        }
    }

    private class MousePressHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.isPrimaryButtonDown()) {
                initialCoordsX = mouseEvent.getSceneX();
                initialCoordsY = mouseEvent.getSceneY();

                double gridX = initialCoordsX / CELL_SIZE;
                double gridY = initialCoordsY / CELL_SIZE;
                controller.pluckPiece(gridX, gridY);
            }
        }
    }

    // TODO: Implement
    private class MouseReleaseHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (controller.hasPieceSelected()) {
                controller.placePiece();
            }
        }
    }

    // TODO: Implement
    // Note: Model shouldn't update the view
    private class KeyPressHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {

        }
    }
}
