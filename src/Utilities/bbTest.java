package Utilities;

public class bbTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BoundingBox test1 = new BoundingBox(0, 0, 36, 36);
		BoundingBox test2 = new BoundingBox(0, 0, 36, 36);
		System.out.println(test1.equals(test2));
		
		test1.getList()[1].addToGlobalOffset(3, 3);
		System.out.println(test1.equals(test2));
	}

}
