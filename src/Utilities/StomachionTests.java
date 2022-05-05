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
	
	void testCheckWin() {
		
	}

	@Test
	void testPluckPiece() {

		Piece[] piece = new Piece[1];
		ArrayList<int[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new int[3]);
		Controller controller = new Controller((observable, obj) -> {
			piece[0] = ((Piece[]) obj)[0];
		}, textures);

		controller.pluckPiece(-1, -1);
		assertFalse(controller.hasPieceSelected());
		controller.pluckPiece(piece[0].getGlobalX(), piece[0].getGlobalY());
		assertTrue(controller.hasPieceSelected());
	}
	
	void testHighlight() {
		
	}
	
	@Test
	void testPieceSelectedFalseOnInit() {
		int[] a = {999};
		ArrayList<int[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new int[3]);
        Controller controller = new Controller((observable, obj) -> a[0] = 444, textures);
        
        assertFalse(controller.hasPieceSelected());
	}
	
	void testPieceSelectedTrue() {
		int[] a = {999};
		ArrayList<int[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new int[3]);
        Controller controller = new Controller((observable, obj) -> a[0] = 444, textures);
        controller.pluckPiece(0,0);
        assertFalse(controller.hasPieceSelected());
	}
	
	void testCheckPlacement() {
		
	}
	
	void testUpdateSelection() {
		
	}

}