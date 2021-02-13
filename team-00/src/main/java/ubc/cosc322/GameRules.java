package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;


public class GameRules {
	public static int[][] board;


	GameRules(ArrayList<Integer> arr){
		//making a 10by10 board and then getting rid of the left most column and bottom most row
		this.board = new int[10][10];
		int row = 0;
		int counter =0;
		int column = 0;
		for(int i =12;i<arr.size();i++) {
			//skiping the left most column
			if(counter>9) {
				column =0;
				counter =0;
				row++;
				continue;
			}
			
			this.board[row][column] = arr.get(i);
			column++;
			counter ++;
			
		}
		
		
	}
	public static ArrayList<Integer> makeMove() {
		
		ArrayList<Integer> current = findCurrent();
		int oldrow =current.get(0);
		int oldcol =current.get(1);
		System.out.println(oldrow + " " + oldcol);
		ArrayList<Integer> move = new ArrayList<Integer>(
				Arrays.asList(current.get(0)+1,current.get(1))
				);
		board[oldrow][oldcol-1] =2;
		System.out.println(move);
		board[oldrow-1][oldcol-1] = 0;
		for(int i =0; i<10;i++) {
			System.out.println();
			for(int j =0; j<10;j++) {
				System.out.print(board[i][j] + " ");
				//System.out.println("row: "+ i+ " col: "+ j + " value: "+board[i][j]);
			}
		}
		System.out.println();
		return move;
		
	}
	
	public static ArrayList<Integer> findCurrent(){
		//find all the twos on the board and see where they can go 
		//White player - 2
		//Black player 1
		ArrayList<Integer> current;
		
		for(int i=0;i<10;i++) {
			for(int j = 0;j<10;j++) {
				if(board[i][j] == 2) {
					current = new ArrayList<Integer>(
							Arrays.asList(i+1,j+1));
					
					return current;
				}
				
			}
		}
		
		
		return null;
		
		
		
	}


}
