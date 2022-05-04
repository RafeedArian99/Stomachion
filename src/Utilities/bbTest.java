package Utilities;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class bbTest extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BoundingBox test1 = new BoundingBox();
		BoundingBox test2 = new BoundingBox();
		System.out.println(test1.equals(test2));
		
		test1.getList()[1].addToGlobalOffset(3, 3);
		System.out.println(test1.equals(test2));
		
		Piece test = new Piece(2, Color.RED);
		System.out.println(test1.encapsulates(test));
		test.rotateAbout(0, 0, false);
		System.out.println(test1.encapsulates(test));
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
