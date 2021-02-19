package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

	int nRows = 11;
	int nCols = 11;
	int nMaxWQueens = 4;
	int nMaxBQueens = 4;
	
	BoardTile[][] board = new BoardTile[nRows][nCols];
	
	//queen id and index-1 in list are identical
	List<Queen> wQueens = new ArrayList<Queen>();
	List<Queen> bQueens = new ArrayList<Queen>();
	//set AI team
	boolean AI_isWhite;
	
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
		System.out.println(state);
		for(int i =0;i<state.size();i++) {
			//skipping the left most column
			if(counter>=11) {
				column =0;
				counter =0;
				row++;
			}
			//generate a tile
			int[] tileIndex = new int[2];
			tileIndex[0] = row;
			tileIndex[1] = column;
			this.board[row][column] = new BoardTile(tileIndex);
			//set queen black
			if(state.get(i) == 1 && nBQueens < nMaxBQueens) {
				nBQueens++;
				Queen q = new Queen(nBQueens, false, tileIndex);
				bQueens.add(q);
				board[row][column].setQueen(q);
			}
			//set queen white
			if(state.get(i) == 2 && nWQueens < nMaxWQueens) {
				nWQueens++;
				Queen q = new Queen(nBQueens, true, tileIndex);
				wQueens.add(q);
				board[row][column].setQueen(q);
			}
			column++;
			counter ++;
		}
		System.out.println("Board initialized");
		printBoard();
	}
	
	public void setAIColor(boolean isWhite) {
		this.AI_isWhite = isWhite;
	}
	public List<Queen> getQueens(){
		if(AI_isWhite) {
			return wQueens;
		}else {
			return bQueens;
		}

	}
	/*
	 * Update the board for any movement. start, end, and arrow
	 */
	public void updateBoard(ArrayList<Integer> orig, ArrayList<Integer> move, ArrayList<Integer> arrow)
	{
		BoardTile start = board[orig.get(0)][orig.get(1)];
		BoardTile end = board[move.get(0)][move.get(1)];
		Queen q = start.getQueen();
		q.setPos(move);
		start.removeQueen();
		end.setQueen(q);
		board[arrow.get(0)][arrow.get(1)].setArrow(true);
	
		printBoard();
	}
	//Dont know whats going on but t.getQueen().isWhite is returning null and crashing the program
	private void printBoard() {
		//implement if wanted once we know what arrow is represented as from server
		BoardTile t = null;
		for(int row=0; row<nRows; row++) {
			for(int col=0; col<nCols; col++) {
				t = board[row][col];
				
				if(t.containsQueen()) {
					if(t.getQueen().isWhite) {
						System.out.print("2");
					}else {
						System.out.print("1");
					}
				}
				else if(t.containsArrow()) {
					System.out.print("X");
				}else {
					System.out.print("0");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
