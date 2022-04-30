package Utilities;

import java.util.ArrayList;
import java.util.HashSet;

public class BoundingBox {
	
	private Piece[] pieceList;
	
	private Vertex globalOffset; 
	
	private double width;
	
	private double height;
	
	// TODO THIS DOES NOT NEED ANY ARGS NOW
	public BoundingBox(double x1, double y1, double width, double height) {
		this.pieceList = new Piece[14];
		for (int i = 0; i < 14; i++) {
			Piece newPiece = new Piece(i);
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
