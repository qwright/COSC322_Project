package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

public class Queen {
	
	ArrayList<Integer> currentPos;
	int id;
	boolean isWhite;

	//create an amazon with default pos and team
	public Queen(int id, boolean isWhite, int[] start_pos)
	{
		this.id = id;
		this.isWhite = isWhite;
		this.currentPos = new ArrayList<>(Arrays.asList(start_pos[0],start_pos[1]));
	}
	
	//returns a list of current tiles that the queen may move to given the current board state
	//Possible moves are calculated from queens current position and if there is a queen or arrow in the path
	//further possible moves are blocked
	public ArrayList<ArrayList<Integer>> availableTiles(BoardTile[][] board)
	{
		
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		int rowPos = currentPos.get(0);
		int colPos = currentPos.get(1);
		
		//Horizontal -right from current
		for(int i=colPos; i < 10; i++) {
			if(i==colPos) {
				continue;
			}
			if(!board[rowPos][i].containsArrow() && !board[rowPos][i].containsQueen()) {
				ArrayList<Integer> tile = new ArrayList<Integer>();
				tile.add(rowPos+1);
				tile.add(i+1);
				moves.add(tile);
			}else {
				break;
			}
		}
		//Horizontal -left from current
		for(int i=colPos; i > -1; i--) {
			if(i==colPos) {
				continue;
			}
			if(!board[rowPos][i].containsArrow() && !board[rowPos][i].containsQueen()) {
				ArrayList<Integer> tile = new ArrayList<Integer>();
				tile.add(rowPos+1);
				tile.add(i+1);
				moves.add(tile);
			}else {
				break;
			}
		}
		//Vertical- up from current;
		for(int i=rowPos; i < 10; i++) {
			if(i==rowPos) {
				continue;
			}
			if(!board[i][colPos].containsArrow() && !board[i][colPos].containsQueen()) {
				ArrayList<Integer> tile = new ArrayList<Integer>();
				tile.add(i+1);
				tile.add(colPos+1);
				moves.add(tile);
			}else {
				break;
			}
		}
		//Vertical- down from current;
		for(int i=rowPos; i > -1; i--) {
			if(i==rowPos) {
				continue;
			}
			if(!board[i][colPos].containsArrow() && !board[i][colPos].containsQueen()) {
				ArrayList<Integer> tile = new ArrayList<Integer>();
				tile.add(i+1);
				tile.add(colPos+1);
				moves.add(tile);
			}else {
				break;
			}
		}
		//(0,0) to (9,9) diag
		//checking if rows on columns will max out first
		//possible moves moving up and right from queen
		//since both are incrementing they will have max-cur tiles left to move
		//columns will max out first
		if(9-rowPos>9-colPos) {
			int tempRow = rowPos;
			for(int i=colPos; i < 10; i++) {
				if(i==colPos) {
					tempRow++;
					continue;
				}
				if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
					ArrayList<Integer> tile = new ArrayList<Integer>();
					tile.add(tempRow+1);
					tile.add(i+1);
					moves.add(tile);
				}else {
					break;
				}
				tempRow++;
			}
		//rows and columns have same amount of room left or columns have more
		}else {
			int tempCol = colPos;
			for(int i=rowPos; i < 10; i++) {
				if(i==rowPos) {
					tempCol++;
					continue;
				}
				if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
					ArrayList<Integer> tile = new ArrayList<Integer>();
					tile.add(i+1);
					tile.add(tempCol+1);
					moves.add(tile);
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
				for(int i=colPos; i > -1; i--) {
					if(i==colPos) {
						tempRow--;
						continue;
					}
					if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
						ArrayList<Integer> tile = new ArrayList<Integer>();
						tile.add(tempRow+1);
						tile.add(i+1);
						moves.add(tile);
					}else {
						break;
					}
					tempRow--;
				}
			//rowPos is same index as colPos or smaller so it will max out first
			}else {
				int tempCol = colPos;
				for(int i=rowPos; i > -1; i--) {
					if(i==rowPos) {
						tempCol--;
						continue;
					}
					if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
						ArrayList<Integer> tile = new ArrayList<Integer>();
						tile.add(i+1);
						tile.add(tempCol+1);
						moves.add(tile);
					}else {
						break;
					}
					tempCol--;
			}
			}
		
		//(0,9) to (9,0)
			//possible moves left(col--) and up(row++) from queen
				if(9-rowPos>colPos) {
					//row has more moves so col will max out first
					int tempRow = rowPos;
					for(int i=colPos; i >-1 ; i--) {
						if(i==colPos) {
							tempRow++;
							continue;
						}
						if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
							ArrayList<Integer> tile = new ArrayList<Integer>();
							tile.add(tempRow+1);
							tile.add(i+1);
							moves.add(tile);
						}else {
							break;
						}
						tempRow++;
					}
				//rowPos will max out first
				}else {
					int tempCol = colPos;
					for(int i=rowPos; i < -1; i--) {
						if(i==colPos) {
							tempCol++;
							continue;
						}
						if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
							ArrayList<Integer> tile = new ArrayList<Integer>();
							tile.add(i+1);
							tile.add(tempCol+1);
							moves.add(tile);
						}else {
							break;
						}
						tempCol++;
				}
				}
				
				//possible moves right(col++) and down(row--) from queen
				if(rowPos>9-colPos) {
					//row is higher index so col will max out first
					int tempRow = rowPos;
					for(int i=colPos; i <10 ; i++) {
						if(i==colPos) {
							tempRow--;
							continue;
						}
						if(!board[tempRow][i].containsArrow() && !board[tempRow][i].containsQueen()) {
							ArrayList<Integer> tile = new ArrayList<Integer>();
							tile.add(tempRow+1);
							tile.add(i+1);
							moves.add(tile);
						}else {
							break;
						}
						tempRow--;
					}
				//rowPos will max out first
				}else {
					int tempCol = colPos;
					for(int i=rowPos; i > -1; i--) {
						if(i==rowPos) {
							tempCol++;
							continue;
						}
						if(!board[i][tempCol].containsArrow() && !board[i][tempCol].containsQueen()) {
							ArrayList<Integer> tile = new ArrayList<Integer>();
							tile.add(i+1);
							tile.add(tempCol+1);
							moves.add(tile);
						}else {
							break;
						}
						tempCol++;
				}
				}
		
		return moves;
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<Integer> getCurrentPos()
	{
		ArrayList<Integer> boardAdjusted = new ArrayList<Integer>(
				Arrays.asList(currentPos.get(0)+1,currentPos.get(1)+1));
		return boardAdjusted;
	}
	
	public void setPos(ArrayList<Integer> pos) {
		this.currentPos.set(0, pos.get(0)-1);
		this.currentPos.set(1,pos.get(1)-1);
	}
}
