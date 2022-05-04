package View;

import Utilities.Piece;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

import Controller.Controller;

import java.util.Observable;
import java.util.Observer;

public class View extends Application implements Observer {
    private Controller controller;
    private Canvas mainCanvas;
    private Scene scene;

    // Selection Canvas variables
    private Canvas selectionCanvas;
    private double initialMouseX, initialMouseY;
    private boolean previouslySelected;

    // Window settings
    private static final double CELL_SIZE = 25;
    private static final double WINDOW_SIZE = CELL_SIZE * 36;
    private static final double BOARD_SIZE = CELL_SIZE * 12;

    // Line settings
    private static final double LINE_WIDTH = 2;
    private static final Color NEUTRAL_LINE_COLOR = Color.rgb(36, 36, 36);
    private static final Color VALID_LINE_COLOR = Color.rgb(74, 255, 0);
    private static final Color INVALID_LINE_COLOR = Color.rgb(128, 0, 0);

    // Color settings
    private static final Color BG_COLOR = Color.grayRgb(230);
    private static final Color FG_COLOR = Color.WHITE;

    // TODO: Delete these variables
    Piece[] pieces;

    public static void main(String args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stomachion");

        // Initialize all canvasses
        GraphicsContext gc;

        mainCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = mainCanvas.getGraphicsContext2D();
        gc.setLineWidth(LINE_WIDTH);
        gc.setLineCap(StrokeLineCap.ROUND);
        mainCanvas.setOnMousePressed(new MousePressHandler());
        mainCanvas.setOnMouseMoved(new MouseMoveHandler());

        selectionCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = selectionCanvas.getGraphicsContext2D();
        gc.setLineWidth(LINE_WIDTH);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setFill(Color.rgb(0, 255, 0, 0.2)); // TODO: Delete
        gc.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE); // TODO: Delete
        selectionCanvas.setLayoutX(WINDOW_SIZE);
        selectionCanvas.setLayoutY(WINDOW_SIZE);

        Canvas gridCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = gridCanvas.getGraphicsContext2D();
        gc.setFill(FG_COLOR);
        gc.fillRect(BOARD_SIZE, BOARD_SIZE, BOARD_SIZE, BOARD_SIZE);
        gc.setFill(NEUTRAL_LINE_COLOR);
        for (int i = 0; i < 37; i++) {
            for (int j = 0; j < 37; j++) {
                gc.fillOval(i * CELL_SIZE - LINE_WIDTH / 2, j * CELL_SIZE - LINE_WIDTH / 2, LINE_WIDTH, LINE_WIDTH);
            }
        }
        gc.setStroke(NEUTRAL_LINE_COLOR);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeRect(BOARD_SIZE, BOARD_SIZE, BOARD_SIZE, BOARD_SIZE);

        // Show initial setup
        Group group = new Group();
        group.getChildren().addAll(gridCanvas, mainCanvas, selectionCanvas);
        scene = new Scene(group, WINDOW_SIZE, WINDOW_SIZE);
        scene.setFill(BG_COLOR);
        scene.setOnMouseDragged(new MouseDragHandler());
        scene.setOnMouseReleased(new MouseReleaseHandler());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        controller = new Controller(this);
    }

    @Override
    public void update(Observable o, Object piecesRaw) {
        assert piecesRaw instanceof Piece[];

        mainCanvas.getGraphicsContext2D().clearRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);

        Piece[] pieces = (Piece[]) piecesRaw;
        Piece highlightedPiece = null;
        boolean hasSelected = false;
        for (Piece piece : pieces) {
            if (piece.getHighlightState() == Piece.PieceState.NEUTRAL)
                drawPiece(piece, mainCanvas);
            else {
                highlightedPiece = piece;
                if (piece.isSelected())
                    hasSelected = true;
            }
        }

        if (highlightedPiece != null) {
            if (highlightedPiece.isSelected() && !previouslySelected) {
                previouslySelected = true;
                selectionCanvas.setLayoutX(0);
                selectionCanvas.setLayoutY(0);
                drawPiece(highlightedPiece, selectionCanvas);
            }
            else {
                drawPiece(highlightedPiece, mainCanvas);
                scene.setCursor(Cursor.MOVE);
            }
        }
        else {
            scene.setCursor(Cursor.DEFAULT);
        }
    }

    private void drawPiece(Piece piece, Canvas canvas) {
        double[][] allCoords = piece.getAllCoords(CELL_SIZE);
        double[] xCoords = allCoords[0];
        double[] yCoords = allCoords[1];

        // Fill piece
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(piece.getColor());
        gc.fillPolygon(xCoords, yCoords, xCoords.length);

        // Draw piece border
        switch (piece.getHighlightState()) {
            case VALID:
                gc.setStroke(VALID_LINE_COLOR);
                break;
            case INVALID:
                gc.setStroke(INVALID_LINE_COLOR);
                break;
            default:
                gc.setStroke(NEUTRAL_LINE_COLOR);
        }

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
            controller.highlight(gridX, gridY);
        }
    }

    private class MousePressHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.isPrimaryButtonDown()) {
                initialMouseX = mouseEvent.getSceneX();
                initialMouseY = mouseEvent.getSceneY();

                double gridX = initialMouseX / CELL_SIZE;
                double gridY = initialMouseY / CELL_SIZE;
                controller.pluckPiece(gridX, gridY);
            }
        }
    }

    private class MouseDragHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            if (controller.hasPieceSelected()) {
                double mouseX = mouseEvent.getSceneX();
                double mouseY = mouseEvent.getSceneY();

                selectionCanvas.setLayoutX(mouseX - initialMouseX);
                selectionCanvas.setLayoutY(mouseY - initialMouseY);
                controller.updateSelectedPosition(mouseX / CELL_SIZE, mouseY / CELL_SIZE);
            }
        }
    }

    private class MouseReleaseHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (previouslySelected) {
                previouslySelected = false;
                GraphicsContext gc = selectionCanvas.getGraphicsContext2D();
                gc.clearRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);
                gc.setFill(Color.rgb(0, 255, 0, 0.2)); // TODO: Delete
                gc.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE); // TODO: Delete
                selectionCanvas.setLayoutX(WINDOW_SIZE);
                selectionCanvas.setLayoutY(WINDOW_SIZE);
                controller.placePiece();
            }
        }
    }

    private class KeyPressHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {
            System.out.println("Here: " + ((Canvas) keyEvent.getSource()).getLayoutBounds());
        }
    }
}
