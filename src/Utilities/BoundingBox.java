/*
 * Name: BoundingBox.java
 *
 * Purpose: This contains the pieces, places them, and has methods related to the board.
 */

package Utilities;

import java.util.ArrayList;
import java.util.Random;

public class BoundingBox {

    // The pieces for the game
    private Piece[] pieceList;

    /**
     * Creates the bounding box and places all of the shapes with random positions,
     * colors, and orientations.
     */
    public BoundingBox(ArrayList<double[]> textures) {
        Random random = new Random();
        long seed = 8645372004284565361L; // random.nextLong();
        System.out.println("Seed: " + seed); // Broken seed: 8645372004284565361
        random = new Random(seed);

        // creates the array to put the pieces in
        this.pieceList = new Piece[14];
        Piece centerBoard = new Piece(14, null);
        centerBoard.addToGlobalOffset(12, 12);

        // iterates through pieces
        for (int i = 0; i < 14; i++) {

            // makes a new piece
            Piece newPiece = new Piece(i, textures.remove(random.nextInt(14 - i)));

            // randomizes orientation
            int rotate = random.nextInt(4);
            int flip = random.nextInt(2);
            for (int count = 0; count < rotate; count++) {
                newPiece.rotateAbout(0, 0, false);
            }
            if (flip == 0) {
                newPiece.flipAbout(0, 0, false);
            }

            int counter = 0;
            while (counter < 100) {

                // places the piece in a random spot
                newPiece.resetGlobalOffset();
                int x = random.nextInt(37);
                int y = random.nextInt(37);
                newPiece.addToGlobalOffset(x, y);
                boolean checker = false;

                // checks if the positioning is valid
                if (!this.encapsulates(newPiece) || centerBoard.collidesWith(newPiece, true)) {
                    checker = true;
                }
                else for (int j = 0; j < i; j++) {
                    if (newPiece.collidesWith(pieceList[j], true)) {
                        checker = true;
                    }
                }

                if (!checker) {
                    counter = 100;
                }
                counter++;
            }

            pieceList[i] = newPiece;
        }
    }

    /**
     * Returns the list of puzzle pieces
     *
     * @return an array of pieces
     */
    public Piece[] getList() {
        return pieceList;
    }

    /**
     * This function checks if a piece is fully within the center of the board.
     *
     * @param piece piece
     * @return true if the piece is fully within the center board
     */
    public boolean encapsulatesCenter(Piece piece) {
        boolean checker = true;
        double[][] coords = piece.getAllCoords();
        for (int i = 0; i < coords.length; i++) {
            if (!(coords[0][i] >= 12 && coords[0][i] <= 24 && coords[1][i] >= 12 && coords[1][i] <= 24)) {
                checker = false;
            }
        }
        return checker;
    }

    /**
     * This function checks if the board properly contains a piece
     * it should be fully within the board but outside of the center
     * square
     *
     * @return whether the piece is properly contained
     */
    public boolean encapsulates(Piece piece) {
        double[][] pieceCoords = piece.getAllCoords();

        // checks if the piece is fully on the board
        for (double[] row : pieceCoords) {
            for (double num : row) {
                if (num < 0 || num >= 36) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Compares the bounding box to another bounding box object
     *
     * @return true if they are the same
     */
    public boolean equals(BoundingBox otherBox) {
        boolean check = true;
        for (int i = 0; i < 14; i++) {
            if (!(this.pieceList[i].equals(otherBox.getList()[i]))) {
                check = false;
            }
        }
        return check;
    }
}