
package Utilities;

import java.util.ArrayList;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class BoundingBox {
	
	private Piece[] pieceList;
	
	private Vertex globalOffset;
	
	private double width;
	
	private double height;
	
	// TODO THIS DOES NOT NEED ANY ARGS NOW
	public BoundingBox(double x1, double y1, double width, double height) {
		Image texturePack = new Image("/Textures/final-14-1x.png");
		PixelReader pixelReader = texturePack.getPixelReader();

		ArrayList<Color> colors = new ArrayList<>();
		for (int i = 0; i < 14; i++) {
			colors.add(pixelReader.getColor(i, 0));
		}

		this.pieceList = new Piece[14];
		for (int i = 0; i < 14; i++) {
			//System.out.println("for1");
			Piece newPiece = new Piece(i, colors.remove((int) (Math.random() * (14 - i))));
			
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
		this.globalOffset = new Vertex(x1, y1);
		this.width = width;
		this.height = height;
	}
	
	public Piece[] getList() {
		return pieceList;
	}
	
	// this function will check if a piece is in the center board
	public boolean encapsulatesCenter(Piece piece) {
		
		return false;
	}
	
	public boolean encapsulates(Piece piece) {
		double[][] pieceCoords = piece.getAllCoords(1);
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