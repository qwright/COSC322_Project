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
	//copy constructor
	public Queen(Queen q)
	{
		this.currentPos = q.getCurrentPos();
		this.id = q.getId();
		this.isWhite = q.isWhite();
		this.availableMoves = q.getMoves();
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
	
	public boolean isWhite()
	{
		return isWhite;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentPos == null) ? 0 : currentPos.hashCode());
		result = prime * result + id;
		result = prime * result + (isWhite ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Queen other = (Queen) obj;
		if (currentPos == null) {
			if (other.currentPos != null)
				return false;
		} else if (!currentPos.equals(other.currentPos))
			return false;
		if (id != other.id)
			return false;
		if (isWhite != other.isWhite)
			return false;
		return true;
	}
	
	//returns a list of current tiles that the queen may move to given the current board state
	//Possible moves are calculated from queens current position and if there is a queen or arrow in the path
	//further possible moves are blocked
	

}
