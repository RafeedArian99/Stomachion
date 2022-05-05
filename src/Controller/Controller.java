package Controller;

import Model.Model;

import java.util.Observer;

public class Controller {
    private final Model model;

    public Controller(Observer observer, String textures) {
        model = new Model(observer, textures);
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

    public void rotateAbout(double x, double y, boolean dir) {
        model.rotateAbout(x, y, dir);
    }

    public void flipAbout(double x, double y, boolean dir) {
        model.flipAbout(x, y, dir);
    }
}
