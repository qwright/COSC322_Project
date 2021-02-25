package ubc.cosc322;

import java.util.ArrayList;

public class Arrow {

		//In the most basic form, this class generates possbile spaces for the arrow given the board state (this is all possible tiles and not queens line of sight)
		public static ArrayList<int[]> availArrowLocations(BoardTile[][] board) {
			ArrayList<int[]> targets = new ArrayList<int[]>();
			for(int i=0; i<10; i++) {
				for(int j=0; j < 10; j++) {
					BoardTile currTile = board[i][j];
					if(!currTile.containsArrow() && !currTile.containsQueen()) {
						targets.add(currTile.index);
					}
				}
			}
			return targets;
		}
}
