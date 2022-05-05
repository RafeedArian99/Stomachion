package Controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Observer;

import org.junit.jupiter.api.Test;

import Model.Model;

class StomachionTests {

	@Test
	void testAddObserverToModel() {
		final int[] a = {999};

		ArrayList<int[]> textures = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			textures.add(new int[3]);

        Controller controller = new Controller((observable, obj) -> a[0] = 444, textures);
        System.out.println();
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
