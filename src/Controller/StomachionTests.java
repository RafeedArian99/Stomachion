package Controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Observer;

import org.junit.jupiter.api.Test;

import Model.Model;

class StomachionTests {

	@Test
	void testAddObserverToModel() {
		final int[] a = {999};
        Controller controller = new Controller((observable, obj) -> a[0] = 444, "");
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
