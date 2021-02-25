package ubc.cosc322;

public class BoardTile {
	
	private boolean hasArrow;
	private boolean hasQueen;
	private Queen queen = null;
	
	public int[] index;
	
	public BoardTile(int[] index) 
	{
		this.index = index;
		hasArrow = false;
		hasQueen = false;
	}

	public boolean containsQueen() {
		return hasQueen;
	}

	public void setQueen(Queen q) {
		this.queen = q;
		this.hasQueen = true;
	}

	public boolean containsArrow() {
		return hasArrow;
	}

	public void setArrow(boolean hasArrow) {
		this.hasArrow = hasArrow;
	}
	
	public Queen getQueen()
	{
		return queen;
	}
	
	public void removeQueen()
	{
		queen = null;
		hasQueen = false;
	}
	
}
