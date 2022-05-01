package Controller;

import Model.Model;

import java.util.Observer;

public class Controller {
    private Model model;

    public Controller(Observer observer) {
        model = new Model(observer);
    }

    public void pluckPiece(double gridX, double gridY) {
    }

    public void highlight(double gridX, double gridY) {
        model.highlight(gridX, gridY);
    }

    public boolean hasPieceSelected() {
        return model.hasPieceSelected();
    }

    public void placePiece() {
    }

    public void checkPlacement(double gridX, double gridY) {
        model.checkPlacement(gridX, gridY);
    }
}
