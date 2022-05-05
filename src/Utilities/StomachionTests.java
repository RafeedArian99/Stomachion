/*
 * @author: Ethan Carrasco
 * 
 * JUnit tests are in here for the controller.
 */
package Utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Observer;

import org.junit.jupiter.api.Test;

import Controller.Controller;
import Model.Model;

class StomachionTests {

	@SuppressWarnings("unused")
	@Test
	void testAddObserverToModel() {
		int[] a = {999};
		ArrayList<int[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new int[3]);
        Controller controller = new Controller((observable, obj) -> a[0] = 444, textures);
        //System.out.println(a[0]);
        assertEquals(a[0], 444);	
    }
	
	void testPluckPiece() {
		
	}
	
	void testHighlight() {
		
	}
	
	void testPieceSelectedTrue() {
		
	}
	
	void testPieceSelectedFalse() {
		
	}
	
	void testCheckPlacement() {
		
	}
	
	void testUpdateSelection() {
		
	}

}
