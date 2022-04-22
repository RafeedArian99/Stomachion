package View;

import Utilities.Piece;
import Utilities.Vertex;

public class test {
    public static void main(String[] args) {
        Piece piece = new Piece(0);
        System.out.println(piece);
        System.out.println(piece.encapsulates(0.5f, 0.5f));
        System.out.println(piece.encapsulates(2f, 0.5f));
        System.out.println(piece.encapsulates(0.5f, -2));
    }
}
