package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class GameBoard{

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
	
	// copy constructor
	public GameBoard(GameBoard gb)
	{
		this.board = gb.copyBoard(gb.getBoard());
		this.AI_isWhite = gb.getAiColor();
		this.wQueens = gb.copyWQueens(gb.getWQueens());
		this.bQueens = gb.copyBQueens(gb.getBQueens());
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
		//System.out.println("Board initialized");
		//printBoard();
	}
	
	public void setAIColor(boolean isWhite) {
		this.AI_isWhite = isWhite;
	}
	
	public boolean getAiColor()
	{
		return this.AI_isWhite;
	}
	
	public BoardTile[][] getBoard(){
		return board;
	}
	//to be used in copy constructor
	public BoardTile[][] copyBoard(BoardTile[][] b)
	{
		BoardTile[][] copyB = new BoardTile[nRows][nCols];
		for(int i=0; i < nRows; i++) {
			for(int j=0; j < nCols; j++) {
				copyB[i][j] = new BoardTile(b[i][j]);
			}
		}
		return copyB;
	}
	
	public List<Queen> getWQueens(){
			return wQueens;
	}
	
	public List<Queen> copyWQueens(List<Queen> wql){
		List<Queen> copyWQ = new ArrayList<Queen>();
		for(Queen q : wql) {
			copyWQ.add(new Queen(q));
		}
		return copyWQ;
	}
	
	public List<Queen> getBQueens()
	{
		return bQueens;
	}
	
	public List<Queen> copyBQueens(List<Queen> bql){
		List<Queen> copyBQ = new ArrayList<Queen>();
		for(Queen q : bql) {
			copyBQ.add(new Queen(q));
		}
		return copyBQ;
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
		//System.out.println("game board updated with arrow position");
		//printBoard();
	}
	
	//update board without shooting arrow to find possible arrow shots
	public void updateBoard(ArrayList<Integer> orig, ArrayList<Integer> move)
	{
		BoardTile start = board[orig.get(0)][orig.get(1)];
		BoardTile end = board[move.get(0)][move.get(1)];
		Queen q = start.getQueen();
		q.setPos(move);
		start.removeQueen();
		end.setQueen(q);
		//System.out.println("game board updated without arrow position");
		//printBoard();
	}
	
	public ArrayList<ArrayList<Integer>> getMoves(BoardTile[][] board, ArrayList<Integer> queenpos) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
				
		getLine(board, queenpos, moves, 1, 0); //up
		getLine(board, queenpos, moves, 0, 1); //down
		getLine(board, queenpos, moves, -1, 0); //left
		getLine(board, queenpos, moves, 0, -1); //right
		
		getLine(board, queenpos, moves, 1, 1); //NE
		getLine(board, queenpos, moves, -1, 1); //NW
		getLine(board, queenpos, moves, 1, -1); //SE
		getLine(board, queenpos, moves, -1, -1); //SW
		
		return moves;
	}
	
	public void getLine(BoardTile[][] board, ArrayList<Integer> queenpos, ArrayList<ArrayList<Integer>> moves, int change_x, int change_y) {
		int start_x = queenpos.get(0);
		int start_y = queenpos.get(1);
		int next_x = start_x + change_x;
		int next_y = start_y + change_y;
		//very hacky
		BoardTile nextTile;
		try {
			nextTile = board[next_x][next_y];
		}catch(Exception e) {
			nextTile = null;
		}
		while(nextTile != null && !nextTile.containsArrow() && !nextTile.containsQueen() && next_x !=0 & next_y !=0) {
			ArrayList<Integer> validPos = new ArrayList<Integer>();
			validPos.add(next_x);
			validPos.add(next_y);
			moves.add(validPos);
			next_x += change_x;
			next_y += change_y;
			try {
				nextTile = board[next_x][next_y];
			}catch(Exception e) {
				break;
			}
		}
	}
	/*
	public ArrayList<ArrayList<Integer>> getMoves(BoardTile[][] board, ArrayList<Integer> Queenpos) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>(96); //max moves possible initially
		
		
		
		int rowPos = Queenpos.get(0);
		int colPos = Queenpos.get(1);
		
		//Horizontal -right from current
		for(int i=colPos; i < 11; i++) {
			if(i==colPos) {
				continue;
			}
			if(!board[rowPos][i].containsArrow() && !board[rowPos][i].containsQueen()) {		
				ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
				tileToAdd.add(rowPos);
				tileToAdd.add(i);
				moves.add(tileToAdd);
			}else {
				break;
			}
		}
		//Horizontal -left from current
		for(int i=colPos; i > 0; i--) {
			if(i==colPos) {
				continue;
			}
			if(!board[rowPos][i].containsArrow() && !board[rowPos][i].containsQueen()) {
				ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
				tileToAdd.add(rowPos);
				tileToAdd.add(i);
				moves.add(tileToAdd);
			}else {
				break;
			}
		}
		//Vertical- up from current;
		for(int i=rowPos; i < 11; i++) {
			if(i==rowPos) {
				continue;
			}
			if(!board[i][colPos].containsArrow() && !board[i][colPos].containsQueen()) {
				ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
				tileToAdd.add(i);
				tileToAdd.add(colPos);
				moves.add(tileToAdd);
			}else {
				break;
			}
		}
		//Vertical- down from current;
		for(int i=rowPos; i > 0; i--) {
			if(i==rowPos) {
				continue;
			}
			if(!board[i][colPos].containsArrow() && !board[i][colPos].containsQueen()) {
				ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
				tileToAdd.add(i);
				tileToAdd.add(colPos);
				moves.add(tileToAdd);
			}else {
				break;
			}
		}
		//(0,0) to (9,9) diag
		//checking if rows on columns will max out first
		//possible moves moving up and right from queen
		//since both are incrementing they will have max-cur tiles left to move
		//columns will max out first
		if(10-rowPos>10-colPos) {
			int tempRow = rowPos;
			for(int i=colPos; i < 11; i++) {
				if(i==colPos) {
					tempRow++;
					continue;
				}
				if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
					ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
					tileToAdd.add(tempRow);
					tileToAdd.add(i);
					moves.add(tileToAdd);
				}else {
					break;
				}
				tempRow++;
			}
		//rows and columns have same amount of room left or columns have more
		}else {
			int tempCol = colPos;
			for(int i=rowPos; i < 11; i++) {
				if(i==rowPos) {
					tempCol++;
					continue;
				}
				if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
					ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
					tileToAdd.add(i);
					tileToAdd.add(tempCol);
					moves.add(tileToAdd);
				}else {
					break;
				}
				tempCol++;
		}
		}
		
		//(0,0) to (9,9) diag
		//moving (col--) and (row--)	
			if(rowPos>colPos) {
				//column is at lower index then row so will go out of bounds first
				int tempRow = rowPos;
				for(int i=colPos; i > 0; i--) {
					if(i==colPos) {
						tempRow--;
						continue;
					}
					if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
						ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
						tileToAdd.add(tempRow);
						tileToAdd.add(i);
						moves.add(tileToAdd);
					}else {
						break;
					}
					tempRow--;
				}
			//rowPos is same index as colPos or smaller so it will max out first
			}else {
				int tempCol = colPos;
				for(int i=rowPos; i > 0; i--) {
					if(i==rowPos) {
						tempCol--;
						continue;
					}
					if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
						ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
						tileToAdd.add(i);
						tileToAdd.add(tempCol);
						moves.add(tileToAdd);
					}else {
						break;
					}
					tempCol--;
			}
			}
		
		//(0,9) to (9,0)
			//possible moves left(col--) and up(row++) from queen
				if(10-rowPos>colPos) {
					//row has more moves so col will max out first
					int tempRow = rowPos;
					for(int i=colPos; i >0 ; i--) {
						if(i==colPos) {
							tempRow++;
							continue;
						}
						if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
							ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
							tileToAdd.add(tempRow);
							tileToAdd.add(i);
							moves.add(tileToAdd);
						}else {
							break;
						}
						tempRow++;
					}
				//rowPos will max out first
				}else {
					int tempCol = colPos;
					for(int i=rowPos; i > 0; i--) {
						if(i==colPos) {
							tempCol++;
							continue;
						}
						if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
							ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
							tileToAdd.add(i);
							tileToAdd.add(tempCol);
							moves.add(tileToAdd);
						}else {
							break;
						}
						tempCol++;
				}
				}
				
				//possible moves right(col++) and down(row--) from queen
				if(rowPos>10-colPos) {
					//row is higher index so col will max out first
					int tempRow = rowPos;
					for(int i=colPos; i <11 ; i++) {
						if(i==colPos) {
							tempRow--;
							continue;
						}
						if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
							ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
							tileToAdd.add(tempRow);
							tileToAdd.add(i);
							moves.add(tileToAdd);
						}else {
							break;
						}
						tempRow--;
					}
				//rowPos will max out first
				}else {
					int tempCol = colPos;
					for(int i=rowPos; i > 0; i--) {
						if(i==rowPos) {
							tempCol++;
							continue;
						}
						if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
							ArrayList<Integer> tileToAdd = new ArrayList<Integer>();
							tileToAdd.add(i);
							tileToAdd.add(tempCol);
							moves.add(tileToAdd);
						}else {
							break;
						}
						tempCol++;
				}
				}
				if(moves.isEmpty()) {
					moves=null;
				}
				
				return moves;
	}
	*/
	public void printBoard() {
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
