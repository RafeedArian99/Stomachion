package Controller;

import Model.Model;

import java.util.ArrayList;
import java.util.Observer;

public class Controller {
    private final Model model;

    /**
     * Creates a Controller using the Observer interface and a list of textures.
     * These are passed to the Model during the constructor call.
     * @param observer: Observer type object to be passed through to Model.
     * @param textures: list of textures to be passed through to Model.
     */
    public Controller(Observer observer, ArrayList<double[]> textures) {
        model = new Model(observer, textures);
    }

    /**
     * This function picks up a piece at the specified coordinates on the grid.
     * @param gridX: a double representing the X coordinate to attempt to grab a piece at.
     * @param gridY: a double representing the Y coordinate to attempt to grab a piece at.
     */
    public void pluckPiece(double gridX, double gridY) {
        model.pluckPiece(gridX, gridY);
    }

    /**
     * This function highlights the piece at the specified coords on the grid.
     * @param gridX: a double representing the X coordinate to attempt to highlight a piece at.
     * @param gridY: a double representing the Y coordinate to attempt to highlight a piece at.
     */
    public void highlight(double gridX, double gridY) {
        model.highlight(gridX, gridY);
    }

    /**
     * This function returns true if a piece is being held by the cursor
     * currently, and false otherwise.
     * @return
     */
    public boolean hasPieceSelected() {
        return model.hasPieceSelected();
    }

    /**
     * Places a piece if valid.
     */
    public void placePiece() {
        model.placePiece();
    }

    /**
     * Checks if the Placement of the selected piece is valid.
     * @param gridX: a double representing the X coordinate to attempt to place a piece at.
     * @param gridY: a double representing the Y coordinate to attempt to place a piece at.
     */
//    public void checkPlacement(double gridX, double gridY) {
//        model.checkPlacement(gridX, gridY);
//    }

    
    public void updateSelectedPosition(double gridX, double gridY) {
        model.updateSelectedPosition(gridX, gridY);
    }

    /**
     * This function rotates the selected piece about a specified axis.
     * @param x: X coordinate of the selected piece
     * @param y: Y coordinate of the selected piece
     * @param dir: axis to rotate around as boolean for clockwise=true, CCW=false.
     */
    public void rotateAbout(double x, double y, boolean dir) {
        model.rotateAbout(x, y, dir);
    }

    /**
     * This function flips the selected piece across a specified axis.
     * @param x: X coordinate of selected piece
     * @param y: Y coordinate of selected piece
     * @param dir: axis to flip across. Horizontal for true, Vertical for false.
     */
    public void flipAbout(double x, double y, boolean dir) {
        model.flipAbout(x, y, dir);
    }

    /**
     * Makes call to Model's checkWin function, returns a boolean
     * that is true if the player found a valid solution.
     * @return true if player found a solution
     */
    public boolean checkWin() {
        return model.checkWin();
    }
}