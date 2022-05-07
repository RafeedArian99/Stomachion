/*
 * Name: Model.java
 *
 * Purpose: Contains all the elements for the game
 */

package Model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Utilities.*;
import Utilities.Piece.PieceState;

public class Model extends Observable {

    private final BoundingBox mainBox;

    private boolean selectionHas;

    private Piece selected;

    private double selectedInitialX, selectedInitialY;
    private double selectedGlobalX, selectedGlobalY;
    private double prevValidX, prevValidY;

    /**
     * Constructs the model
     *
     * @param observer object
     * @param textures list of colors (defined by int array of r,g,b).
     */
    public Model(Observer observer, ArrayList<double[]> textures) {
        // TODO THIS DOES NOT NEED ANY ARGS NOW
        this.mainBox = new BoundingBox(textures);

        this.addObserver(observer);
        setChanged();
        notifyObservers(mainBox.getList());
    }

    /**
     * Checks if the game is won
     *
     * @return true if the game is won
     */
    public boolean checkWin() {
        boolean checker = true;
        Piece[] pieces = this.mainBox.getList();

        // Iterates through the pieces to see if they are all in the center board.
        for (Piece piece : pieces) {
            if (!(this.mainBox.encapsulatesCenter(piece))) {
                checker = false;
            }
        }
        return checker;
    }

    /**
     * Picks up a piece
     *
     * @param gridX x-coordinate
     * @param gridY y-coordinate
     */
    public void pluckPiece(double gridX, double gridY) {
        Piece[] array = this.mainBox.getList();
        for (int i = 0; i < 14; i++) {
            if (array[i].encapsulates(gridX, gridY)) {
                array[i].setSelected(true);
                this.selectionHas = true;
                this.selected = array[i];

                selectedInitialX = gridX;
                selectedInitialY = gridY;
                selectedGlobalX = selected.getGlobalX();
                selectedGlobalY = selected.getGlobalY();

                break;
            }
        }
        setChanged();
        notifyObservers(mainBox.getList());
    }

    /**
     * Highlights a piece
     *
     * @param gridX coordinate
     * @param gridY coordinate
     */
    public void highlight(double gridX, double gridY) {
        Piece[] array = this.mainBox.getList();

        boolean highlighted = false;
        for (int i = 0; i < 14; i++) {
            if (!highlighted && array[i].encapsulates(gridX, gridY)) {
                array[i].highlight(PieceState.VALID);
                highlighted = true;
            }
            else {
                array[i].highlight(PieceState.NEUTRAL);
            }
        }

        setChanged();
        notifyObservers(this.mainBox.getList());
    }

    /**
     * Returns a boolean with whether a piece is selected
     *
     * @return true if a piece is selected
     */
    public boolean hasPieceSelected() {
        return this.selectionHas;
    }

    /**
     * Places a piece
     */
    public void placePiece() {
        this.selected.setSelected(false);
        this.selectionHas = false;
        this.selected.setGlobalOffset(prevValidX, prevValidY);
        this.selected.highlight(PieceState.NEUTRAL);
        this.selected.snapGlobalOffset();

        setChanged();
        notifyObservers(this.mainBox.getList());
    }

    /**
     * Sets the offset of the selected piece while it is in motion
     *
     * @param gridX coordinate
     * @param gridY coordinate
     */
    public void updateSelectedPosition(double gridX, double gridY) {
        selected.setGlobalOffset(selectedGlobalX + gridX - selectedInitialX, selectedGlobalY + gridY - selectedInitialY);
        selected.snapGlobalOffset();

        selected.highlight(PieceState.VALID);
        for (Piece piece : mainBox.getList()) {
            if (piece != selected && piece.collidesWith(selected, false)) {
                selected.highlight(PieceState.INVALID);
                break;
            }
        }

        if (selected.getHighlightState() != PieceState.INVALID) {
            prevValidX = selectedGlobalX + gridX - selectedInitialX;
            prevValidY = selectedGlobalY + gridY - selectedInitialY;
        }

        selected.setGlobalOffset(selectedGlobalX + gridX - selectedInitialX, selectedGlobalY + gridY - selectedInitialY);
        setChanged();
        notifyObservers(mainBox.getList());
    }

    /**
     * Rotates a selected piece
     *
     * @param x   coordinate
     * @param y   coordinate
     * @param dir to rotate
     */
    public void rotateAbout(double x, double y, boolean dir) {
        if (this.selectionHas) {
            selected.rotateAbout(x, y, dir);
            selectedInitialX = x;
            selectedInitialY = y;
            selectedGlobalX = selected.getGlobalX();
            selectedGlobalY = selected.getGlobalY();
        }
    }

    /**
     * Flips a selected piece
     *
     * @param x   coordinate
     * @param y   coordinate
     * @param dir to flip
     */
    public void flipAbout(double x, double y, boolean dir) {
        if (this.selectionHas) {
            selected.flipAbout(x, y, dir);
            selectedInitialX = x;
            selectedInitialY = y;
            selectedGlobalX = selected.getGlobalX();
            selectedGlobalY = selected.getGlobalY();
        }
    }

//    public void checkPlacement(double gridX, double gridY) {
//        double prevGlobalX = selected.getGlobalX();
//        double prevGlobalY = selected.getGlobalY();
//        selected.snapGlobalOffset();
//
//        selected.highlight(PieceState.VALID);
//        for (Piece piece : mainBox.getList()) {
//            if (piece.collidesWith(selected)) {
//                System.out.println("INVALID");
//                selected.highlight(PieceState.INVALID);
//                break;
//            }
//        }
//
//        selected.setGlobalOffset(prevGlobalX, prevGlobalY);
//    }
}