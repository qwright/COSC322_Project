package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
	
	BoardTile[][] board;
	int nRows = 10;
	int nCols = 10;
	int nMaxWQueens = 4;
	int nMaxBQueens = 4;
	
	//queen id and index-1 in list are identical
	List<Queen> wQueens = new ArrayList<Queen>();
	List<Queen> bQueens = new ArrayList<Queen>();
	
	public GameBoard(ArrayList<Integer> state)
	{
		initBoard(state);
		
	}
	
	private void initBoard(ArrayList<Integer> state)
	{
		int nWQueens = 0, nBQueens = 0;
		
		int row = 0;
		int counter = 0;
		int column = 0;
		for(int i =12;i<state.size();i++) {
			//skipping the left most column
			if(counter>9) {
				column =0;
				counter =0;
				row++;
				continue;
			}
			//generate a tile
			int[][] currIdx = new int[row][column];
			int[] qPos = new int[2];
			qPos[0] = row;
			qPos[1] = column;
			this.board[row][column] = new BoardTile(qPos);
			//set queen black
			if(state.get(i) == 1 && nBQueens < nMaxBQueens) {
				nBQueens++;
				Queen q = new Queen(nBQueens, false, qPos);
				bQueens.add(q);
				board[row][column].setQueen(q,true);
			}
			//set queen white
			if(state.get(i) == 2 && nWQueens < nMaxWQueens) {
				nWQueens++;
				Queen q = new Queen(nBQueens, false, qPos);
				bQueens.add(q);
				board[row][column].setQueen(q,true);
			}
			column++;
			counter ++;
		}
	}
	/*
	 * Update the board. Looks for opposing players move (one arrow and one queen move)
	 */
	public void updateBoard(ArrayList<Integer> state, boolean isWhite)
	{
		List <int[]> updatedQPos = new ArrayList<int[]>();
		//loop through state once and update arrow position, get queen coordinates
		for(int row=0; row<nRows; row++) {
			for(int col=0; col<nCols; col++) {
				BoardTile currTile = board[row][col];
				int[] position = new int[2];
				position[0] = row;
				position[1] = col;
				//update arrow what number in state indicates arrow????
				if(state.get(row*10 + col) == 3 && !currTile.containsArrow()){
					currTile.setArrow(true); 
				}
				if(state.get(row*10+col) == 2 && isWhite) {
					int[] pos = new int[2];
					pos[0] = row;
					pos[1] = col;
					updatedQPos.add(pos);
				}
				else if(state.get(row*10+col) == 1 && !isWhite) {
					int[] pos = new int[2];
					pos[0] = row;
					pos[1] = col;
					updatedQPos.add(pos);
				}
				
			}
		}
		//if White player, look for at black piece
		//PS: probably really bad logic here
		Queen moved = null;
		//find queen that has moved
		for(Queen q:wQueens) {
			if(!updatedQPos.contains(q.currentPos)) {
				moved = q;
				break;
			}
		}
		//remove other possible positions
		for(int i=0; i < updatedQPos.size(); i++) {
			for(Queen q: wQueens) {
				if(q.getCurrentPos() == updatedQPos.get(i)) {
					updatedQPos.remove(i);
					continue;
				}
			}
		}
		//update previous tile, queen, and tile queen occupies now
		board[moved.getCurrentPos()[0]][moved.getCurrentPos()[1]].removeQueen();
		moved.setPos(updatedQPos.get(0));
		board[updatedQPos.get(0)[0]][updatedQPos.get(0)[1]].setQueen(moved, true);
	}
	
	private void printBoard() {
		//implement if wanted once we know what arrow is represented as from server
	}
}
