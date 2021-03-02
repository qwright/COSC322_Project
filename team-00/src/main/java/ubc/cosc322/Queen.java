package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

public class Queen {
	
	ArrayList<Integer> currentPos;
	ArrayList<ArrayList<Integer>> availableMoves;
	int id;
	boolean isWhite;

	//create an amazon with default pos and team
	public Queen(int id, boolean isWhite, int[] start_pos)
	{
		this.id = id;
		this.isWhite = isWhite;
		this.currentPos = new ArrayList<>(Arrays.asList(start_pos[0],start_pos[1]));
	}
	public int getId() {
		return id;
	}
	
	public ArrayList<Integer> getCurrentPos()
	{
		ArrayList<Integer> boardAdjusted = new ArrayList<Integer>(
				Arrays.asList(currentPos.get(0),currentPos.get(1)));
		return boardAdjusted;
	}
	
	public void setPos(ArrayList<Integer> pos) {
		this.currentPos.set(0,pos.get(0));
		this.currentPos.set(1,pos.get(1));
	}
	
	public ArrayList<ArrayList<Integer>> getMoves(){
		return availableMoves;
	}
	
	//returns a list of current tiles that the queen may move to given the current board state
	//Possible moves are calculated from queens current position and if there is a queen or arrow in the path
	//further possible moves are blocked
	

}
