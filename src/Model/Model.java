package Model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Utilities.*;
import Utilities.Piece.PieceState;

public class Model extends Observable {
	
	private BoundingBox mainBox;
	
	private boolean selectionHas;
	
	private Piece selected;
	
	public Model(Observer observer) {
		// TODO THIS DOES NOT NEED ANY ARGS NOW
		this.mainBox = new BoundingBox(0, 0, 36, 36);

		this.addObserver(observer);
		setChanged();
		notifyObservers(mainBox.getList());
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
    		}
    		else {
    			array[i].highlight(PieceState.NEUTRAL);
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
	
	// plucking removes from the main box and puts into the selection box
	// placing removes form the selection box and places in the man box.private BoundingBox mainBox;private BoundingBox mainBox;private BoundingBox mainBox;
	
}
