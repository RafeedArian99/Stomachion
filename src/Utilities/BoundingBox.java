/*
 * Name: BoundingBox.java
 * 
 * Purpose: This contains the pieces, places them, and has methods related to the board.
 * 
 * Author: Joshua Klein, CSC335
 */

package Utilities;

import java.util.ArrayList;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class BoundingBox {
	
	// The pieces for the game
	private Piece[] pieceList;
	
	/**
	 * Creates the bounding box and places all of the shapes with random positions,
	 * colors, and orientations.
	 */
	public BoundingBox(ArrayList<int[]> textures) {
//		Image texturePack = new Image("/Textures/" + textures);
//		PixelReader pixelReader = texturePack.getPixelReader();

		this.pieceList = new Piece[14];
		for (int i = 0; i < 14; i++) {
			//System.out.println("for1");
			Piece newPiece = new Piece(i, textures.remove((int) (Math.random() * (14 - i))));
			
			int rotate = (int)(Math.random() * 4);
			int flip = (int)(Math.random() * 2);
			for (int count = 0; count < rotate; count++) {
				newPiece.rotateAbout(0, 0, false);
			}
			if (flip == 0) {
				newPiece.flipAbout(0, 0, false);
			}
			
			int counter = 0;
			while (counter < 100) {
				//System.out.println("while1");
				newPiece.resetGlobalOffset();
				int x = (int)(Math.random() * 36);
				int y = (int)(Math.random() * 36);
				newPiece.addToGlobalOffset(x, y);
				boolean checker = false;
				if (!(this.encapsulates(newPiece))) {
					checker = true;
				}
				for (int j = 0; j < i; j++) {
					//System.out.println("for2");
					if (newPiece.collidesWith(pieceList[j])) {
						checker = true;
					}
				}
				if (checker == false) {
					counter = 100;
				}
				counter++;
			}
			
			pieceList[i] = newPiece;
		}
	}
	
	/**
	 * 
	 * Returns the list of puzzle pieces
	 * 
	 * @return an array of pieces
	 */
	public Piece[] getList() {
		return pieceList;
	}
	
	// this function will check if a piece is fully within the center board
	public boolean encapsulatesCenter(Piece piece) {
		boolean checker = true;
		double[][] coords = piece.getAllCoords();
		for(int i = 0; i < 14; i++) {
			if (!(coords[0][i] >= 12 && coords[0][i] <= 24 && coords[1][i] >= 12 && coords[1][i] <= 24)) {
				checker = false;
			}
		}
		return checker;
	}
	
	// this function will check if a piece has ANY overlap with the center of the board
	public boolean overlapCenter(Piece piece) {
		
		return false;
	}
	
	/**
	 * 
	 * This function checks if the board properly contains a piece
	 * it should be fully within the board but outside of the center
	 * square
	 *
	 * @return whether the piece is properly contained
	 */
	public boolean encapsulates(Piece piece) {
		double[][] pieceCoords = piece.getAllCoords();
		for (int i = 0; i < pieceCoords[0].length; i++) {
			//System.out.println("for3");
			
			
			
			// FIX THIS: RN THIS JUST MAKES THE INNER BOX CHECK FOR VERTEX RATHER THAN EDGE COLLISIONS
			
			
			
			if (pieceCoords[0][i] > 12 && pieceCoords[0][i] < 24 && pieceCoords[1][i] > 12 && pieceCoords[1][i] < 24 ) {
				return false;
			}
			
			
			
			
			
			
		}
		for (double[] row : pieceCoords) {
			//System.out.println("for4");
			for (double num : row) {
				//System.out.println("for5");
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