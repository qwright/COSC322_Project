package ubc.cosc322;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
//general idea
//check if node has been visited before, if no simulate random moves till end of game and return
//total amount of actions it took and propogate this back up the tree setting node score+=value on the way up
//
public class MCTS {

	int score;
//shoudl take a copy of the game board
	
	public ArrayList<Integer> run(GameBoard board,Queen queen) {
	
	ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
	ArrayList<Integer> queenPos = queen.getCurrentPos();
	
	ArrayList<ArrayList<Integer>> queenmoves = board.getMoves(board.getBoard(), queenPos);
	
	for(ArrayList<Integer> queenmove: queenmoves) {
		
		
			board.updateBoard(queenPos, queenmove);
		
			ArrayList<ArrayList<Integer>> arrowshots = board.getMoves(board.getBoard(), queenmove);
			
		
			for(int i =0; i<= arrowshots.size();i++) {
				if(i==arrowshots.size()) {
					//revert back
					board.updateBoard(queenmove, queenPos);
					continue;
				}
					
				List<Integer> move = Stream.of(queenmove,arrowshots.get(i))
						.flatMap(x -> x.stream())
						.collect(Collectors.toList());
				moves.add((ArrayList)move);
	
			}
	}
	Node treeRootNode = new Node(null,queenPos);
	
	
	//adding first layer of moves for root node
	for(ArrayList<Integer> move: moves) {
		addChild(treeRootNode,move);
	}
	
	
	Node current = treeRootNode;
	for(int i=0;i<2000;i++) {
		
		System.out.println(current.getChildren());
		if(current.isLeaf()) {
			if(current.getVisits()==0) {
				System.out.println("doing rollout");
				int score = rollout(board,current);
				backpropegate(current,score);
			}else {
				expandNode(board,current);
			}
		}else {
			current = getNextNode(current);
		}
	}
	ArrayList<Integer> bestMove = null;
	for(Node child: treeRootNode.getChildren()) {
		int best =0;
		if(child.getScore()>best) {
			best = child.getScore();
			bestMove = child.getPosition();
		}
			
	}
	printTree(treeRootNode," ");
	return bestMove;

	}
	 private static void printTree(Node node, String appender) {
		  System.out.println(appender + node.getPosition());
		  for (Node each : node.getChildren()) {
		   printTree(each, appender + appender);
		  }
		 }
	private double getUCB1(Node node) {
		if(node.getVisits() ==0) {
			return 1000000;
		}
		int visits = node.getVisits();
		int parentVisits = node.getParent().getVisits();
		int score =node.getScore();
		return score + 2*Math.sqrt((Math.log(parentVisits)/visits));
	}
	
	private Node getNextNode(Node parentNode) {
		List<Node> children = parentNode.getChildren();
		double score = 0;
		Node nextNode = null;
		
		//looping through nodes and will return the position of the node(acting as unique identifier for the node)
		for(Node child: children) {
			double UCB1 = getUCB1(child);
			if(UCB1>score) {
				nextNode = child;
			}
			
		}
		return nextNode;
	}
	
	public static void expandNode(GameBoard board, Node node) {
		//will need to update the board to the state of the node that is to be expanded and then get all moves

		ArrayList<Integer> queenpos = node.getPosition();
		board.updateBoard(node.getParent().getPosition(), queenpos, node.getParent().getPosition());
		ArrayList<ArrayList<Integer>> moves = board.getMoves(board.getBoard(), queenpos);
		for(ArrayList<Integer> move: moves) {
			addChild(node,move);
		}
	}
	
	private static void backpropegate(Node child,int score) {
		
		while(child.getParent()!= null) {
			child.updateVists();
			child.updateScore(score);
			child=child.getParent();
		}
		//increment the root node
		child.updateScore(score);
		child.updateVists();
		
	}
	private static int rollout(GameBoard board,Node node) {
		//try rolling out only using one player to move
		ArrayList<Integer> queenpos = node.getPosition();
		ArrayList<Integer>nextQueenPos;
		BoardTile[][] currentBoard = board.getBoard();
		//updating the board so the select queen is now moved to the current node not the root
		board.updateBoard(node.getParent().getPosition(),queenpos ,node.getParent().getPosition());
		//zero moves have been made
		int count = 0;
		while(board.getMoves(currentBoard, queenpos)!=null) {
			//update board with first move
			ArrayList<ArrayList<Integer>> nextMove = board.getMoves(currentBoard, queenpos);
			nextQueenPos = nextMove.get((int)Math.random() * nextMove.size());
			board.updateBoard(queenpos, nextQueenPos, queenpos);
			queenpos = nextQueenPos;
			count++;
			
		}
		
		
		return count;
		
			
			
		}
	
	private static Node addChild(Node parent, ArrayList<Integer> nodePosition) {
		Node node = new Node(parent,nodePosition);
		parent.getChildren().add(node);
		return node;
	}
	
}











