package Utilities;

public class bbTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

//		BoundingBox test1 = new BoundingBox();
//		BoundingBox test2 = new BoundingBox();
//		System.out.println(test1.equals(test2));
//
//		test1.getList()[1].addToGlobalOffset(3, 3);
//		System.out.println(test1.equals(test2));
//
//		Piece test = new Piece(2, Color.RED);
//		System.out.println(test1.encapsulates(test));
//		test.rotateAbout(0, 0, false);
//		System.out.println(test1.encapsulates(test));
        Piece piece = new Piece(0, new double[3]);
        Piece piece1 = new Piece(2, new double[3]);
        piece1.setGlobalOffset(0.5, 2);
    }

}
