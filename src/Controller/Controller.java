package Controller;

import Model.Model;

import java.util.Observer;

public class Controller {
    private final Model model;

    public Controller(Observer observer) {
        model = new Model(observer);
    }

    public void pluckPiece(double gridX, double gridY) {
        model.pluckPiece(gridX, gridY);
    }

    public void highlight(double gridX, double gridY) {
        model.highlight(gridX, gridY);
    }

    public boolean hasPieceSelected() {
        return model.hasPieceSelected();
    }

    public void placePiece() {
        model.placePiece();
    }

    public void checkPlacement(double gridX, double gridY) {
        model.checkPlacement(gridX, gridY);
    }

    public void updateSelectedPosition(double gridX, double gridY) {
        model.updateSelectedPosition(gridX, gridY);
    }
}
