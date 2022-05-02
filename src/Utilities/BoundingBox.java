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
			Piece newPiece = new Piece(i, colors.remove((int) (Math.random() * (14 - i))));
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
	public boolean encapsulates(Piece piece) {
		
		return false;
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
