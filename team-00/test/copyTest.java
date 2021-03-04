import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import ubc.cosc322.*;

import org.junit.Test;


public class copyTest {
	
	private void generate_testArr(int[] arr, ArrayList<Integer> a) {
		for(int i : arr) {
			a.add(i);
		}
	}
	
	@Test
	public void gameboard_notEqualTest()
	{
		int[] arr = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0};
		ArrayList<Integer> initalBoard = new ArrayList<Integer>();
		generate_testArr(arr, initalBoard);
		GameBoard gb = new GameBoard(initalBoard);
		GameBoard gb_copy = new GameBoard(gb);
		gb_copy.setAIColor(true);
		
		assertNotEquals(gb.getAiColor(), gb_copy.getAiColor());
	}
	
	@Test 
	public void queen_notEqualTest()
	{
		ArrayList<Integer> testPos = new ArrayList<Integer>();
		testPos.add(1);
		testPos.add(2);
		Queen q = new Queen(1,true,new int[] {1,1});
		Queen q_copy = new Queen(q);
		q_copy.setPos(testPos);
		
		assertNotEquals(q.getCurrentPos(), q_copy.getCurrentPos());
	}
	
	@Test 
	public void tile_notEqualTest()
	{
		BoardTile bt = new BoardTile(new int[] {1,1});
		BoardTile bt_copy = new BoardTile(bt);
		bt_copy.setArrow(true);
		
		assertNotEquals(bt.containsArrow(), bt_copy.containsArrow());
		
	}
}
