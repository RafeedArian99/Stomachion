package View;

import Utilities.Piece;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

import Controller.Controller;
import javafx.util.Duration;

import java.util.Observable;
import java.util.Observer;

public class View extends Application implements Observer {
    // Variables with multiple access points
    private Controller controller;
    private Canvas mainCanvas;
    private Scene scene;

    // Selection Canvas variables
    private Canvas selectionCanvas;
    private double initialMouseX, initialMouseY;
    private boolean previouslySelected;

    // Animation variables and settings
    private RotateTransition rotateTransition, counterclockwiseRotateTransition;
    private ScaleTransition scaleTransition;
    //    private RotateTransition verticalFlipTransition, horizontalFlipTransition;
    private static final double ANIMATION_DURATION = 100;
    private static final KeyCode CLOCKWISE_ROTATE_KEY = KeyCode.D;
    private static final KeyCode COUNTERCLOCKWISE_ROTATE_KEY = KeyCode.A;
    private static final KeyCode VERTICAL_FLIP_KEY = KeyCode.W;
    private static final KeyCode HORIZONTAL_FLIP_KEY = KeyCode.S;

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
        GraphicsContext gc;

        // Main canvas
        mainCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = mainCanvas.getGraphicsContext2D();
        gc.setLineWidth(LINE_WIDTH);
        gc.setLineCap(StrokeLineCap.ROUND);
        mainCanvas.setOnMousePressed(new MousePressHandler());
        mainCanvas.setOnMouseMoved(new MouseMoveHandler());

        // Selection canvas
        selectionCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = selectionCanvas.getGraphicsContext2D();
        gc.setLineWidth(LINE_WIDTH);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setFill(Color.rgb(0, 255, 0, 0.2)); // TODO: Delete
        gc.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE); // TODO: Delete
        selectionCanvas.setLayoutX(WINDOW_SIZE);
        selectionCanvas.setLayoutY(WINDOW_SIZE);

        // Animations
        rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(ANIMATION_DURATION));
        rotateTransition.setNode(selectionCanvas);

        scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(ANIMATION_DURATION));
        scaleTransition.setNode(selectionCanvas);

//        counterclockwiseRotateTransition = new RotateTransition();
//        counterclockwiseRotateTransition.setDuration(Duration.millis(ANIMATION_DURATION));
//        counterclockwiseRotateTransition.setNode(selectionCanvas);
//        counterclockwiseRotateTransition.setByAngle(-90);

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
        scene.setOnKeyPressed(new KeyPressHandler());
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
        for (Piece piece : pieces) {
            if (piece.getHighlightState() == Piece.PieceState.NEUTRAL)
                drawPiece(piece, mainCanvas, 0, 0);
            else {
                highlightedPiece = piece;
            }
        }

        if (highlightedPiece != null) {
            if (highlightedPiece.isSelected()) {
                previouslySelected = true;
                selectionCanvas.setLayoutX(initialMouseX - WINDOW_SIZE / 2);
                selectionCanvas.setLayoutY(initialMouseY - WINDOW_SIZE / 2);

                drawPiece(highlightedPiece, selectionCanvas, WINDOW_SIZE / 2 - initialMouseX, WINDOW_SIZE / 2 - initialMouseY);
            }
            else {
                drawPiece(highlightedPiece, mainCanvas, 0, 0);
                scene.setCursor(Cursor.MOVE);
            }
        }
        else {
            scene.setCursor(Cursor.DEFAULT);
        }
    }

    private void drawPiece(Piece piece, Canvas canvas, double offsetX, double offsetY) {
        double[][] allCoords = piece.getAllCoords();

        double[] xCoords = allCoords[0];
        for (int i = 0; i < xCoords.length; i++)
            xCoords[i] = xCoords[i] * CELL_SIZE + offsetX;

        double[] yCoords = allCoords[1];
        for (int i = 0; i < yCoords.length; i++) {
            yCoords[i] = yCoords[i] * CELL_SIZE + offsetY;
        }


        // Fill piece
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(piece.getColor());
        gc.fillPolygon(xCoords, yCoords, xCoords.length);

        // Draw piece border
        switch (piece.getHighlightState()) {
            case VALID:
                if (canvas == mainCanvas && piece.isSelected())
                    System.out.println("Here");
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

                selectionCanvas.setLayoutX(mouseX - WINDOW_SIZE / 2);
                selectionCanvas.setLayoutY(mouseY - WINDOW_SIZE / 2);
                KeyPressHandler kps = (KeyPressHandler) scene.getOnKeyPressed();
                kps.mouseX = mouseX;
                kps.mouseY = mouseY;
                controller.updateSelectedPosition(mouseX / CELL_SIZE, mouseY / CELL_SIZE);
            }
        }
    }

    private class MouseReleaseHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (previouslySelected) {
                previouslySelected = false;

                KeyPressHandler kps = (KeyPressHandler) scene.getOnKeyPressed();
                kps.flipX = -2;
                kps.flipY = -2;
                kps.horizontal = false;

                selectionCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
                GraphicsContext gc = selectionCanvas.getGraphicsContext2D();
                gc.setFill(Color.rgb(0, 255, 0, 0.2)); // TODO: Delete
                gc.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE); // TODO: Delete
                selectionCanvas.setLayoutX(WINDOW_SIZE);
                selectionCanvas.setLayoutY(WINDOW_SIZE);

                ((Group) scene.getRoot()).getChildren().set(2, selectionCanvas);
                rotateTransition.setNode(selectionCanvas);
                scaleTransition.setNode(selectionCanvas);

                controller.placePiece();
            }
        }
    }

    private class KeyPressHandler implements EventHandler<KeyEvent> {

        private double mouseX, mouseY;
        private double flipX = -2, flipY = -2;

        private boolean horizontal = false;

        @Override
        public void handle(KeyEvent keyEvent) {
            boolean clockwise, vertical;
            if ((clockwise = keyEvent.getCode() == CLOCKWISE_ROTATE_KEY) || keyEvent.getCode() == COUNTERCLOCKWISE_ROTATE_KEY) {
                rotateTransition.setByAngle(clockwise ? 90 : -90);
                rotateTransition.play();

                horizontal = !horizontal;
                controller.rotateAbout(mouseX / CELL_SIZE, mouseY / CELL_SIZE, clockwise);
            }
            else if ((vertical = keyEvent.getCode() == VERTICAL_FLIP_KEY) || keyEvent.getCode() == HORIZONTAL_FLIP_KEY) {
                if (vertical == horizontal) {
                    scaleTransition.setByX(flipX);
                    scaleTransition.setByY(0);
                    flipX *= -1;
                }
                else {
                    scaleTransition.setByX(0);
                    scaleTransition.setByY(flipY);
                    flipY *= -1;
                }

                scaleTransition.play();
                controller.flipAbout(mouseX / CELL_SIZE, mouseY / CELL_SIZE, Piece.VERTICAL);
            }
        }
    }
}
