package ubc.cosc322;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
//general idea
//check if node has been visited before, if no simulate random moves till end of game and return
//total amount of actions it took and propogate this back up the tree setting node score+=value on the way up
//
public class MCTS {

	int rootVisits;
	
//shoudl take a copy of the game board
	public MCTS(GameBoard board,Queen queen) {
	
	
	ArrayList<Integer> queenPos = queen.getCurrentPos();
	
	ArrayList<ArrayList<Integer>> moves = queen.getMoves();
	
	Node treeRootNode = new Node(null,queenPos);
	
	
	
	//adding first layer of moves for root node
	for(ArrayList<Integer> move: moves) {
		addChild(treeRootNode,move,"depth1");
	}
	
	//node to expand
	Node expansion = getNextNode(treeRootNode);
	
	if(expansion.getVisits()==0) {
		rollout(board)
	}

	}
	
	private double getUBC1(Node node) {
		if(node.getVisits() ==0) {
			return 1000000;
		}
		int visits = node.getVisits();
		int score =node.getScore();
		return score + 2*Math.sqrt((Math.log(rootVisits)/visits));
	}
	
	private Node getNextNode(Node parentNode) {
		List<Node> children = parentNode.getChildren();
		double score = 0;
		Node nextNode = null;
		
		//looping through nodes and will return the position of the node(acting as unique identifier for the node)
		for(Node child: children) {
			double UBC1 = getUBC1(child);
			if(UBC1>score) {
				nextNode = child;
			}
			
		}
		return nextNode;
	}
	



private static int rollout(GameBoard board,Node node) {
	//try rolling out only using one player to move
	Random rand = new Random();
	ArrayList<Integer> queenpos = node.getPosition();
	while(board.getMoves(board, queenpos))
	board.updateBoard(node.getParent().getPosition(),queenpos ,node.getParent().getPosition());
	
	
	
	boolean terminal = false;
	
		
		
	}
	
	
	
}




private static Node addChild(Node parent, ArrayList<Integer> nodePosition, String id) {
	Node node = new Node(parent,nodePosition);
	node.setId(id);
	parent.getChildren().add(node);
	return node;
}




}

