package View;

import Utilities.Piece;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import Controller.Controller;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class View extends Application implements Observer {
    // Variables with multiple access points
    private Controller controller;
    private Canvas mainCanvas;
    private Scene scene;
    private static String textureChosen;
    private Text statusText;

    // Selection Canvas variables
    private SelectionBox selectionBox;

    private static final double ANIMATION_DURATION = 100;
    private static final KeyCode CLOCKWISE_ROTATE_KEY = KeyCode.D;
    private static final KeyCode COUNTERCLOCKWISE_ROTATE_KEY = KeyCode.A;
    private static final KeyCode VERTICAL_FLIP_KEY = KeyCode.W;
    private static final KeyCode HORIZONTAL_FLIP_KEY = KeyCode.S;

    // Window settings
    private static final double CELL_SIZE = 20;
    private static final double WINDOW_SIZE = CELL_SIZE * 36;
    private static final double BOARD_SIZE = CELL_SIZE * 12;

    // Line settings
    private static final double LINE_WIDTH = 2;
    private static final Color NEUTRAL_LINE_COLOR = Color.rgb(36, 36, 36);
    private static final Color VALID_LINE_COLOR = Color.rgb(74, 255, 0);
    private static final Color INVALID_LINE_COLOR = Color.rgb(255, 50, 25);

    // Color settings
    private static final Color BG_COLOR = Color.grayRgb(230);
    private static final Color FG_COLOR = Color.WHITE;

    public static void main(String args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stomachion");
        textureChosen = "final-14-1x.png";

        Group root = new Group();
        int red = (int) (Math.random() * (255 / 2));
        int green = (int) (Math.random() * (255 / 2));
        int blue = (int) (Math.random() * (255 / 2));

        Scene scene = new Scene(root, WINDOW_SIZE, WINDOW_SIZE, Color.rgb(red, green, blue));
        Text text = new Text(105, 200, "STOMACHION");
        Button startButton = new Button("START");

        Image textureImage1 = new Image("/Textures/final-14-1x.png", 280, 20, true, false);
        ImageView image1Texture = new ImageView(textureImage1);
        image1Texture.setFitWidth(280);
        image1Texture.setFitHeight(20);

        Image textureImage2 = new Image("/Textures/blues-14-1x.png", 280, 20, true, false);
        ImageView image2Texture = new ImageView(textureImage2);
        image2Texture.setFitWidth(280);
        image2Texture.setFitHeight(20);

        Image textureImage3 = new Image("/Textures/uofa_colors-14-1x.png", 280, 20, true, false);
        ImageView image3Texture = new ImageView(textureImage3);
        image3Texture.setFitWidth(280);
        image3Texture.setFitHeight(20);

        Button texture1 = new Button("", image1Texture);
        Button texture2 = new Button("", image2Texture);
        Button texture3 = new Button("", image3Texture);

        Text textureSelect = new Text(600, 110, "Texture 1 Selected");
        textureSelect.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
        textureSelect.setFill(Color.WHITE);
        text.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 75));
        text.setFill(Color.LIGHTGRAY);
        startButton.relocate(250, WINDOW_SIZE / 2);

        texture1.relocate(415, 0);
        texture2.relocate(415, 30);
        texture3.relocate(415, 60);

        startButton.setPrefSize(200, 100);
        startButton.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 25));
        startButton.setTextFill(Color.BLUE);

        root.getChildren().add(text);
        root.getChildren().add(startButton);
        root.getChildren().add(texture1);
        root.getChildren().add(texture2);
        root.getChildren().add(texture3);
        root.getChildren().add(textureSelect);

        startButton.setOnAction((event) -> {
            startGame(stage);
        });
        texture1.setOnAction((event) -> {
            textureSelect.setText("Texture 1 Selected");
            textureChosen = "final-14-1x.png";
        });
        texture2.setOnAction((event) -> {
            textureSelect.setText("Texture 2 Selected");
            textureChosen = "blues-14-1x.png";
        });
        texture3.setOnAction((event) -> {
            textureSelect.setText("Texture 3 Selected");
            textureChosen = "uofa_colors-14-1x.png";
        });

        stage.setScene(scene);
        stage.show();
    }

    public void startGame(Stage stage) {
        GraphicsContext gc;

        // Main Canvas + Selection Box
        mainCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = mainCanvas.getGraphicsContext2D();
        gc.setLineWidth(LINE_WIDTH);
        gc.setLineCap(StrokeLineCap.ROUND);

        selectionBox = new SelectionBox();

        // Lay out the grid
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
        statusText = new Text();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMinWidth(WINDOW_SIZE);
        vBox.getChildren().addAll(statusText);
        vBox.setPadding(new Insets(50, 50, 50, 50));

        Group group = new Group();
        group.getChildren().addAll(gridCanvas, mainCanvas, vBox, selectionBox.canvas);

        scene = new Scene(group, WINDOW_SIZE, WINDOW_SIZE);
        scene.setFill(BG_COLOR);
        scene.setOnMouseDragged(new MouseDragHandler());
        scene.setOnMouseReleased(new MouseReleaseHandler());
        scene.setOnKeyPressed(new KeyPressHandler());
        scene.setOnMousePressed(new MousePressHandler());
        scene.setOnMouseMoved(new MouseMoveHandler());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Image image = new Image("/Textures/" + textureChosen);
        PixelReader pixelReader = image.getPixelReader();
        ArrayList<double[]> textures = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Color color = pixelReader.getColor(i, 0);
            textures.add(new double[]{color.getRed(), color.getGreen(), color.getBlue()});
        }

        controller = new Controller(this, textures);
    }

    @Override
    public void update(Observable o, Object piecesRaw) {
        Piece[] pieces = (Piece[]) piecesRaw;
        Piece highlightedPiece = null;
        mainCanvas.getGraphicsContext2D().clearRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);
        for (Piece piece : pieces) {
            if (piece.getHighlightState() == Piece.PieceState.NEUTRAL) {
                double[][] allCoords = piece.getAllCoords();
                drawPiece(piece, mainCanvas, 0, 0, allCoords[0], allCoords[1]);
            }
            else {
                highlightedPiece = piece;
            }
        }

        // Evaluate highlighted piece last to render over other pieces
        if (highlightedPiece != null) {
            if (highlightedPiece.isSelected()) {
                selectionBox.draw(highlightedPiece);
                selectionBox.previouslySelected = true;
            }
            else {
                double[][] allCoords = highlightedPiece.getAllCoords();
                drawPiece(highlightedPiece, mainCanvas, 0, 0, allCoords[0], allCoords[1]);
                scene.setCursor(Cursor.MOVE);
            }
        }
        else {
            scene.setCursor(Cursor.DEFAULT);
        }

        // Set the status text
        if (controller != null && controller.checkWin()) {
            statusText.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 50));
            statusText.setFill(Color.BLACK);
            statusText.setText("You found a solution!");
        }
        else if (highlightedPiece != null && highlightedPiece.getHighlightState() == Piece.PieceState.INVALID) {
            statusText.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 25));
            statusText.setFill(INVALID_LINE_COLOR);
            statusText.setText("Invalid placement");
        }
        else {
            statusText.setText("");
        }
    }

    private void drawPiece(Piece piece, Canvas canvas, double offsetX, double offsetY, double[] xCoords, double[] yCoords) {
//        if (xCoords == null && yCoords == null) {
//            double[][] allCoords = piece.getAllCoords();
//            xCoords = allCoords[0];
//            yCoords = allCoords[1];

        double[] correctedXCoords = new double[xCoords.length];
        for (int i = 0; i < xCoords.length; i++)
            correctedXCoords[i] = xCoords[i] * CELL_SIZE + offsetX;

        double[] correctedYCoords = new double[yCoords.length];
        for (int i = 0; i < yCoords.length; i++) {
            correctedYCoords[i] = yCoords[i] * CELL_SIZE + offsetY;
        }


        // Fill piece
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double[] color = piece.getColor();
        gc.setFill(new Color(color[0], color[1], color[2], 1));
        gc.fillPolygon(correctedXCoords, correctedYCoords, xCoords.length);

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
            double endX = i == xCoords.length - 1 ? correctedXCoords[0] : correctedXCoords[i + 1];
            double endY = i == yCoords.length - 1 ? correctedYCoords[0] : correctedYCoords[i + 1];

            gc.strokeLine(correctedXCoords[i], correctedYCoords[i], endX, endY);
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
                selectionBox.initialX = mouseEvent.getSceneX();
                selectionBox.initialY = mouseEvent.getSceneY();

                double gridX = selectionBox.initialX / CELL_SIZE;
                double gridY = selectionBox.initialY / CELL_SIZE;
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

                selectionBox.moveCenterTo(mouseX, mouseY);
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
            if (selectionBox.previouslySelected) {
                selectionBox.previouslySelected = false;

                KeyPressHandler kps = (KeyPressHandler) scene.getOnKeyPressed();
                kps.flipX = -2;
                kps.flipY = -2;
                kps.horizontal = false;

                selectionBox.reset(false);
                controller.placePiece();
                controller.highlight(mouseEvent.getSceneX() / CELL_SIZE, mouseEvent.getSceneY() / CELL_SIZE);
            }
        }
    }

    private class KeyPressHandler implements EventHandler<KeyEvent> {
        private double mouseX, mouseY;
        private double flipX = -2, flipY = -2;
        private boolean horizontal = false;

        @Override
        public void handle(KeyEvent keyEvent) {
            if (controller.hasPieceSelected()) {
                boolean clockwise, vertical;
                if ((clockwise = keyEvent.getCode() == CLOCKWISE_ROTATE_KEY) || keyEvent.getCode() == COUNTERCLOCKWISE_ROTATE_KEY) {
//                    rotateTransition.setByAngle(clockwise ? 90 : -90);
//                    rotateTransition.play();
                    selectionBox.rotateAnimation.setByAngle(clockwise ? 90 : -90);
                    selectionBox.rotateAnimation.play();

                    horizontal = !horizontal;
                    controller.rotateAbout(mouseX / CELL_SIZE, mouseY / CELL_SIZE, clockwise);
                }
                else if ((vertical = keyEvent.getCode() == VERTICAL_FLIP_KEY) || keyEvent.getCode() == HORIZONTAL_FLIP_KEY) {
                    if (vertical == horizontal) {
                        selectionBox.flipAnimation.setByX(flipX);
                        selectionBox.flipAnimation.setByY(0);
                        flipX *= -1;
                    }
                    else {
                        selectionBox.flipAnimation.setByX(0);
                        selectionBox.flipAnimation.setByY(flipY);
                        flipY *= -1;
                    }

                    selectionBox.flipAnimation.play();
                    controller.flipAbout(mouseX / CELL_SIZE, mouseY / CELL_SIZE, vertical);
                }
            }
        }
    }

    private class SelectionBox {
        private Canvas canvas;
        private double[] xCoords, yCoords;
        private double initialX, initialY;
        private boolean previouslySelected;

        private final RotateTransition rotateAnimation;
        private final ScaleTransition flipAnimation;

        public SelectionBox() {
            rotateAnimation = new RotateTransition();
            rotateAnimation.setDuration(Duration.millis(ANIMATION_DURATION));

            flipAnimation = new ScaleTransition();
            flipAnimation.setDuration(Duration.millis(ANIMATION_DURATION));

            previouslySelected = false;
            this.reset(true);
        }

        public void reset(boolean ignoreScene) {
            canvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(LINE_WIDTH);
            gc.setLineCap(StrokeLineCap.ROUND);

            if (!ignoreScene) ((Group) scene.getRoot()).getChildren().set(3, canvas);
            rotateAnimation.setNode(canvas);
            flipAnimation.setNode(canvas);
        }

        public void moveCenterTo(double mouseX, double mouseY) {
            canvas.setLayoutX(mouseX - WINDOW_SIZE / 2);
            canvas.setLayoutY(mouseY - WINDOW_SIZE / 2);
        }

        public void draw(Piece piece) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);

            if (!previouslySelected) {
                this.moveCenterTo(initialX, initialY);
                double[][] allCoords = piece.getAllCoords();
                xCoords = allCoords[0];
                yCoords = allCoords[1];
            }

            drawPiece(piece, canvas, WINDOW_SIZE / 2 - initialX, WINDOW_SIZE / 2 - initialY, xCoords, yCoords);
        }
    }
}
