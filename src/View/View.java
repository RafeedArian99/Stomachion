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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

    // Selection Canvas variables
    private Canvas selectionCanvas;
    private double initialMouseX, initialMouseY;
    private boolean previouslySelected;

    // Animation variables and settings
    private RotateTransition rotateTransition;
    private ScaleTransition scaleTransition;
    private static final double ANIMATION_DURATION = 100;
    private static final KeyCode CLOCKWISE_ROTATE_KEY = KeyCode.E;
    private static final KeyCode COUNTERCLOCKWISE_ROTATE_KEY = KeyCode.Q;
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
    private static final Color INVALID_LINE_COLOR = Color.rgb(128, 0, 0);

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
        Image textureImage1 = new Image("/Textures/final-14-1x.png");
        ImageView image1Texture = new ImageView(textureImage1);
        image1Texture.setFitWidth(90);
        image1Texture.setFitHeight(20);
        Image textureImage2 = new Image("/Textures/blues-14-1x.png");
        ImageView image2Texture = new ImageView(textureImage2);
        image2Texture.setFitWidth(90);
        image2Texture.setFitHeight(20);
        Image textureImage3 = new Image("/Textures/uofa_colors-14-1x.png");
        ImageView image3Texture = new ImageView(textureImage3);
        image3Texture.setFitWidth(90);
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
        texture1.relocate(610, 0);
        texture2.relocate(610, 30);
        texture3.relocate(610, 60);
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
        selectionCanvas.setLayoutX(WINDOW_SIZE);
        selectionCanvas.setLayoutY(WINDOW_SIZE);

        // Animations
        rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(ANIMATION_DURATION));
        rotateTransition.setNode(selectionCanvas);

        scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(ANIMATION_DURATION));
        scaleTransition.setNode(selectionCanvas);

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

        Image image = new Image("/Textures/" + textureChosen);
        PixelReader pixelReader = image.getPixelReader();
        ArrayList<int[]> textures = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Color color = pixelReader.getColor(i, 0);
            textures.add(new int[]{(int) (color.getRed() * 256), (int) (color.getGreen() * 256), (int) (color.getBlue() * 256)});
        }

        controller = new Controller(this, textures);
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
        int[] color = piece.getColor();
        gc.setFill(Color.rgb(color[0], color[1], color[2]));
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
                controller.flipAbout(mouseX / CELL_SIZE, mouseY / CELL_SIZE, vertical);
            }
        }
    }
}
