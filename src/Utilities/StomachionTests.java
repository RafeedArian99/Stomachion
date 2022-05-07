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
	/**
	 * This test checks that we can actually create a Controller and update the model inside it.
	 */
	void testAddObserverToModel() {
		int[] a = {999};
		ArrayList<double[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new double[3]);
        Controller controller = new Controller((observable, obj) -> a[0] = 444, textures);
        //System.out.println(a[0]);
        assertEquals(a[0], 444);	
    }
	
	
	//TODO: figure out a way to get the win state from the magical wizardry that is observable interactions and everything being private.
	void testCheckWin() {
		
		Piece[] piece = new Piece[1];
		ArrayList<double[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new double[3]);
		
		BoundingBox bb = new BoundingBox(textures);
		Controller controller = new Controller((observable, obj) -> {
			//piece[0] = ((Piece[]) obj)[0];
			//bb
		}, textures);

		assertFalse(controller.checkWin());
	
	}

	@Test
	/**
	 * This test attempts to pick up a piece. Creates a dummy piece and uses its
	 * coordinates as parameters for the controller to pick up the piece.
	 */
	void testPluckPiece() {

		Piece[] piece = new Piece[1];
		ArrayList<double[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new double[3]);
		Controller controller = new Controller((observable, obj) -> {
			piece[0] = ((Piece[]) obj)[0];
		}, textures);

		controller.pluckPiece(-1, -1);
		assertFalse(controller.hasPieceSelected());
		controller.pluckPiece(piece[0].getGlobalX(), piece[0].getGlobalY());
		assertTrue(controller.hasPieceSelected());
	}
	
	@Test
	/**
	 * This test creates a dummy Piece and Controller, updates it using the observable interface,
	 * selects the Piece, and highlights it, then checks that the state was updated to be highlighted. 
	 */
	void testHighlight() {
		Piece[] piece = new Piece[1];
		ArrayList<double[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new double[3]);
		Controller controller = new Controller((observable, obj) -> {
			piece[0] = ((Piece[]) obj)[0];
		}, textures);

		controller.pluckPiece(-1, -1);
		assertFalse(controller.hasPieceSelected());
		controller.highlight(piece[0].getGlobalX(), piece[0].getGlobalY());
		assertTrue(piece[0].getHighlightState()==PieceState.VALID);
	}
	
	@Test
	/**
	 * This test creates a new Controller and checks that a piece is not selected
	 * on initialization of the Controller.
	 */
	void testPieceSelectedFalseOnInit() {
		int[] a = {999};
		ArrayList<double[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new double[3]);
        Controller controller = new Controller((observable, obj) -> a[0] = 444, textures);
        
        assertFalse(controller.hasPieceSelected());
	}

}
