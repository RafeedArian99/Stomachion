package Model;

import java.util.Observable;
import java.util.Observer;

import Utilities.*;
import Utilities.Piece.PieceState;

public class Model extends Observable {
	
	private boolean win = false;
	
	private BoundingBox mainBox;
	
	private boolean selectionHas;
	
	private Piece selected;

	private double selectedInitialX, selectedInitialY;
	private double selectedGlobalX, selectedGlobalY;
	
	public Model(Observer observer, String textures) {
		// TODO THIS DOES NOT NEED ANY ARGS NOW
		this.mainBox = new BoundingBox(textures);

		this.addObserver(observer);
		setChanged();
		notifyObservers(mainBox.getList());
	}
	
	public boolean checkWin() {
		boolean checker = true;
		Piece[] pieces = this.mainBox.getList();
		for (Piece piece : pieces) {
			if (!(this.mainBox.encapsulatesCenter(piece))) {
				checker = false;
			}
		}
		this.win = checker;
		return this.win;
	}

    @SuppressWarnings("deprecation")
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

    public boolean hasPieceSelected() {
        return this.selectionHas;
    }

    public void placePiece() {
        this.selected.setSelected(false);
        this.selectionHas = false;
        this.selected.snapGlobalOffset();

        setChanged();
        notifyObservers(this.mainBox.getList());
    }

    public void checkPlacement(double gridX, double gridY) {
    }

    public void updateSelectedPosition(double gridX, double gridY) {
        selected.setGlobalOffset(selectedGlobalX + gridX - selectedInitialX, selectedGlobalY + gridY - selectedInitialY);
    }

    public void rotateAbout(double x, double y, boolean dir) {
        if (this.selectionHas) {
            selected.rotateAbout(x, y, dir);
            selectedInitialX = x;
            selectedInitialY = y;
            selectedGlobalX = selected.getGlobalX();
            selectedGlobalY = selected.getGlobalY();
        }
    }

    public void flipAbout(double x, double y, boolean dir) {
        if (this.selectionHas) {
            selected.flipAbout(x, y, dir);
            selectedInitialX = x;
            selectedInitialY = y;
            selectedGlobalX = selected.getGlobalX();
            selectedGlobalY = selected.getGlobalY();
        }
    }

    // plucking removes from the main box and puts into the selection box
    // placing removes form the selection box and places in the man box.private BoundingBox mainBox;private BoundingBox mainBox;private BoundingBox mainBox;
}
