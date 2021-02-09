package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameRules {
	public int[][] board;


	GameRules(ArrayList<Integer> arr){
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
			//traversing through row, once reached end of row set counter to 0 and move to next letter
			this.board[row][column] = arr.get(i);
			column++;
			counter ++;
			
		}
		
		for(int i=0;i<10;i++) {
			for(int j = 0;j<10;j++) {
				System.out.println("Row:"+i+" Column:"+j+" value:"+board[i][j]);
				
			}
		}
	}


}
