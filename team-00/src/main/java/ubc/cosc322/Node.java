package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class Node {
	int score;
	int visits;
	int rootVisit; //dont know if this can be a global variable
	private ArrayList<Integer> nodePosition = new ArrayList<>();
	private final List<Node> children = new ArrayList<>();
	private final Node parent;
	
	Node(Node parent,ArrayList<Integer> position){
		score = 0;
		visits = 0;
		this.parent = parent;
		this.nodePosition = position;
	}
	
	public List<Node> getChildren(){
		return children;
	}
	
	public Node getParent() {
		return parent;
	}
	public int getVisits() {
		return visits;
	}
	public int getScore() {
		return score;
	}
	
	public void updateScore(int score) {
		this.score += score;
	}
	
	public void updateVists() {
		this.visits++;
	}
	public boolean isLeaf() {
		if(this.children.isEmpty())
			return true;
		return false;
	}
	
	public ArrayList<Integer> getPosition(){
		return nodePosition;
	}
	
	
}
