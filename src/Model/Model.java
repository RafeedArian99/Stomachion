package Model;

import java.util.ArrayList;
import java.util.Observable;

import Utilities.*;

public class Model extends Observable {
	
	private BoundingBox mainBox;
	
	private BoundingBox selectionBox;
	
	public Model() {
		this.pieceList = new ArrayList<Piece>();
		for (int i = 0; i < 14; i++) {
			Piece newPiece = new Piece(i);
			pieceList.add(newPiece);
		}
		this.box = new BoundingBox();
	}
	
	// selection box, takes in two coordinates as arguments, returns 
	// every piece object, not piece ID, as an ArrayList of pieces,
	// selects all pieces fully within the bounds.
	
	public ArrayList<Piece> selectionBox(double x1, double y1, double x2, double y2) {
		
		
		return this.pieceList;
	}
	
	// plucking removes from the main box and puts into the selection box
	// placing removes form the selection box and places in the man box.
	
}
