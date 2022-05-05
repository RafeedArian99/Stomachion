package View;

import Utilities.Piece;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import Controller.Controller;

import java.util.Observable;
import java.util.Observer;

public class View extends Application implements Observer {
    private Controller controller; // TODO: Instantiate in main
    private Canvas mainCanvas;
    private double initialCoordsX, initialCoordsY;
    private static String textureChosen = "final-14-1x.png";

    private static final double CELL_SIZE = 20;
    private static final double WINDOW_SIZE = CELL_SIZE * 36;
    private static final double BOARD_SIZE = CELL_SIZE * 12;

    private static final double LINE_WIDTH = 2;
    private static final Color NEUTRAL_LINE_COLOR = Color.rgb(36, 36, 36);
    private static final Color VALID_LINE_COLOR = Color.rgb(74, 255, 0);
    private static final Color INVALID_LINE_COLOR = Color.rgb(128, 0, 0);

    private static final Color BG_COLOR = Color.grayRgb(230);
    private static final Color FG_COLOR = Color.WHITE;
    

    // TODO: Delete these variables
    Piece[] pieces;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	stage.setTitle("Stomachion");
    	Group root = new Group();
    	int red = (int) (Math.random()*(255/2));
    	int green = (int) (Math.random()*(255/2));
    	int blue = (int) (Math.random()*(255/2));
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
    	startButton.relocate(250, WINDOW_SIZE/2);
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

        // Initialize all canvasses
        GraphicsContext gc;

        mainCanvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        gc = mainCanvas.getGraphicsContext2D();
        gc.setStroke(NEUTRAL_LINE_COLOR);
        gc.setLineWidth(LINE_WIDTH);
        gc.setLineCap(StrokeLineCap.ROUND);
        mainCanvas.setOnMousePressed(new MousePressHandler());
        mainCanvas.setOnMouseMoved(new MouseMoveHandler());
        mainCanvas.setOnMouseReleased(new MouseReleaseHandler());
        mainCanvas.setOnMouseDragged(new MouseDragHandler());

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
        group.getChildren().addAll(gridCanvas, mainCanvas); //, selectionCanvas);
        Scene scene = new Scene(group, WINDOW_SIZE, WINDOW_SIZE);
        scene.setFill(BG_COLOR);
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
                drawPiece(piece, mainCanvas);
            else
                highlightedPiece = piece;
        }

        if (highlightedPiece != null) {
            drawPiece(highlightedPiece, mainCanvas);
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
                initialCoordsX = mouseEvent.getSceneX();
                initialCoordsY = mouseEvent.getSceneY();

                double gridX = initialCoordsX / CELL_SIZE;
                double gridY = initialCoordsY / CELL_SIZE;
                controller.pluckPiece(gridX, gridY);
            }
        }
    }

    private class MouseDragHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            if (controller.hasPieceSelected()) {
                double gridX = mouseEvent.getSceneX() / CELL_SIZE;
                double gridY = mouseEvent.getSceneY() / CELL_SIZE;

                controller.updateSelectedPosition(gridX, gridY);
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
    private class KeyPressHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {

        }
    }
}
