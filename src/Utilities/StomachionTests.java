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
import Utilities.Piece.PieceState;

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
	
	
	//TODO: figure out a way to get the win state from the magical wizardry that is observable interactions and everything being private.
	void testCheckWin() {
		
		Piece[] piece = new Piece[1];
		ArrayList<int[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new int[3]);
		
		BoundingBox bb = new BoundingBox(textures);
		Controller controller = new Controller((observable, obj) -> {
			//piece[0] = ((Piece[]) obj)[0];
			//bb
		}, textures);

		//controller.pluckPiece(-1, -1);
		assertFalse(controller.checkWin());
		//controller.pluckPiece(piece[0].getGlobalX(), piece[0].getGlobalY());
		//assertTrue(controller.hasPieceSelected());
	
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
	
	@Test
	void testHighlight() {
		Piece[] piece = new Piece[1];
		ArrayList<int[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new int[3]);
		Controller controller = new Controller((observable, obj) -> {
			piece[0] = ((Piece[]) obj)[0];
		}, textures);

		controller.pluckPiece(-1, -1);
		assertFalse(controller.hasPieceSelected());
		controller.highlight(piece[0].getGlobalX(), piece[0].getGlobalY());
		assertTrue(piece[0].getHighlightState()==PieceState.VALID);
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

}
