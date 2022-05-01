package Model;

import java.util.ArrayList;
import java.util.Observable;

import Utilities.*;
import Utilities.Piece.PieceState;

public class Model extends Observable {
	
	private BoundingBox mainBox;
	
	private boolean selectionHas;
	
	private Piece selected;
	
	public Model() {
		// TODO THIS DOES NOT NEED ANY ARGS NOW
		this.mainBox = new BoundingBox(0, 0, 36, 36);
	}
	
    @SuppressWarnings("deprecation")
	public void pluckPiece(double gridX, double gridY) {
    	Piece[] array = this.mainBox.getList();
    	for (int i = 0; i < 14; i++) {
    		if (array[i].encapsulates(gridX, gridY)) {
    			array[i].setSelected(true);
    			this.selectionHas = true;
    			this.selected = array[i];
    			break;
    		}
    	}
    	setChanged();
    	notifyObservers(mainBox.getList());
    }

    public void highlight(double gridX, double gridY) {
    	Piece[] array = this.mainBox.getList();
    	for (int i = 0; i < 14; i++) {
    		if (array[i].encapsulates(gridX, gridY)) {
    			array[i].highlight(PieceState.VALID);
    			break;
    		}
    	}
    	setChanged();
    	notifyObservers(this.mainBox.getList());
    }

    public boolean hasPieceSelected() {
        return this.selectionHas;
    }

    public void placePiece() {
    	this.selected.setSelected(false);
    	this.selectionHas = false;
    	setChanged();
    	notifyObservers(this.mainBox.getList());
    }

    public void checkPlacement(double gridX, double gridY) {
    	
    }
	
    public void test() {
    	
    }
    
	// plucking removes from the main box and puts into the selection box
	// placing removes form the selection box and places in the man box.private BoundingBox mainBox;private BoundingBox mainBox;private BoundingBox mainBox;
	
}
