package Utilities;

import java.util.ArrayList;

public class BoundingBox {
	
	private ArrayList<Piece> pieceList;
	
	private Vertex globalOffset; 
	
	private double width;
	
	private double height;
	
	public BoundingBox(double x1, double y1, double width, double height) {
		this.pieceList = new ArrayList<Piece>(14);
		this.globalOffset = new Vertex(x1, y1);
		this.width = width;
		this.height = height;
	}
	
	// checkContents, returns all pieces within the coordinates of this box
	// uses the getAllCoords from the piece method
	public void updateBox(ArrayList<Piece> pieces){
		this.pieceList = new ArrayList<Piece>(14);
		for (Piece curPiece: pieces) {
			if (this.encapsulates(curPiece)) {
				this.pieceList.add(curPiece);
			}
		}
	}
	
	public boolean encapsulates(Piece piece) {
		
		return false;
	}
}
