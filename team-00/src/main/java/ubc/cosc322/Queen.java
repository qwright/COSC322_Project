package ubc.cosc322;

import java.util.ArrayList;

public class Queen {
	
	int[] currentPos;
	int id;
	boolean isWhite;

	//create an amazon with default pos and team
	public Queen(int id, boolean isWhite, int[] start_pos)
	{
		this.id = id;
		this.isWhite = isWhite;
		this.currentPos = start_pos;
	}
	
	//returns a list of current tiles that the queen may move to given the current board state
	public ArrayList<int[]> availableTiles(BoardTile[][] board)
	{
		ArrayList<int[]> moves = new ArrayList<int[]>();
		int rowPos = currentPos[0];
		int colPos = currentPos[1];
		int[] tile = new int[2];
		//vertical
		for(int i=0; i < 10; i++) {
			if(i==colPos) {
				continue;
			}
			if(!board[rowPos][i].containsArrow() && !board[rowPos][i].containsQueen()) {
				tile[0] = rowPos;
				tile[1] = i;
				moves.add(tile);
			}
		}
		//horizontal;
		for(int i=0; i < 10; i++) {
			if(i==rowPos) {
				continue;
			}
			if(!board[i][colPos].containsArrow() && !board[i][colPos].containsQueen()) {
				tile[0] = i;
				tile[1] = colPos;
				moves.add(tile);
			}
		}
		//diag1
		
		//diag2
		
		return moves;
	}
	
	public int getId() {
		return id;
	}
	
	public int[] getCurrentPos()
	{
		return currentPos;
	}
	
	public void setPos(int[] pos) {
		this.currentPos = pos;
	}
}
