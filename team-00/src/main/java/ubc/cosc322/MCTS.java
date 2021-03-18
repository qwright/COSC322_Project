package ubc.cosc322;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Random;
//general idea
//check if node has been visited before, if no simulate random moves till end of game and return
//total amount of actions it took and propogate this back up the tree setting node score+=value on the way up
//
public class MCTS implements Runnable{

	int bestScore;
	ArrayList<Integer> bestMove = new ArrayList<Integer>();
	GameBoard b;
	Queen q;
	//shoudl take a copy of the game board
	
	public MCTS(GameBoard b,Queen q) 
	{
		this.b = b;
		this.q = q;
	}
	
	@Override
	public void run() {
	GameBoard board = new GameBoard(b);
	Queen queen = new Queen(q);
	
	ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
	ArrayList<Integer> queenPos = queen.getCurrentPos();
	
	ArrayList<ArrayList<Integer>> queenmoves = board.getMoves(board.getBoard(), queenPos);
	
	
	Node treeRootNode = new Node(null,queenPos);

	
	
	Node current = treeRootNode;
	long startTime = System.currentTimeMillis();
	while(System.currentTimeMillis()-startTime<2000) {
		//System.out.println(current.getPosition());
		//System.out.println("checking isLeaf");
		
		if(current.isLeaf()) {
			if(current.getParent()==null)
				expandNode(board,current,treeRootNode);
			if(current.getVisits()==0) {
				//System.out.println("doing rollout");
				int score = rollout(board,current,treeRootNode);
				backpropegate(current,score);
				current = treeRootNode;
				//System.out.println("Here");
				//System.out.println(treeRootNode.getVisits());
				//System.out.println(treeRootNode.getScore());
				
			}else {
				//takes in current board and current node
				expandNode(board,current,treeRootNode);
			}
		}else {
			//System.out.println("finding next node");
			//System.out.println(current.getPosition());
			current = getNextNode(current);
		}
	}
	ArrayList<Integer> potentialMove = null;
	int best =0;
	for(Node child: treeRootNode.getChildren()) {
		potentialMove = child.getPosition(); // this is a workaround to unknown bug where child ends up with illegal position
		System.out.println(child.getScore());
		if(child.getScore()>best && !b.getBoard()[potentialMove.get(0)][potentialMove.get(1)].containsArrow()) {
			best = child.getScore();
			
			potentialMove = child.getPosition();
			
		}
			
	}
	//printTree(treeRootNode," ");
	this.bestScore = best;
	System.out.println(best + " this is the best score with move " + potentialMove);
	this.bestMove= potentialMove;
	System.out.println("MCTS done");
	}

	private double getUCB1(Node node) {
		if(node.getVisits() ==0) {
			return 100000000;
		}
		int visits = node.getVisits();
		int parentVisits = node.getParent().getVisits();
		int score =node.getScore();
		return score + 4*Math.sqrt((Math.log(parentVisits)/visits));
	}
	
	private Node getNextNode(Node parentNode) {
		List<Node> children = parentNode.getChildren();
		double score = 0;
		Node nextNode = null;
		//System.out.println(children);
		//looping through nodes and will return the position of the node(acting as unique identifier for the node)
		for(Node child: children) {
			double UCB1 = getUCB1(child);
			//System.out.println(UCB1);
			//System.out.println(child.getVisits());
			if(UCB1>score) {
				score = UCB1;
				nextNode = child;
			}
			
		}
		//System.out.println("next node");
		//System.out.println(nextNode.getScore());
		return nextNode;
	}
	
	private void expandNode(GameBoard board, Node nodeToMove, Node treeRootNode) {
		//will need to update the board to the state of the node that is to be expanded and then get all moves
		GameBoard expandBoard = new GameBoard(board);
		ArrayList<Integer> queenPosMove = new ArrayList<>(nodeToMove.getPosition().subList(0, 2));
		ArrayList<Integer> queenPosCur = new ArrayList<>(treeRootNode.getPosition().subList(0, 2));
		ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
		//this is clone of original board - moving queen from current pos to next theoretical position
		expandBoard.updateBoard(queenPosCur, queenPosMove);
		//getting all moves from that new position
		ArrayList<ArrayList<Integer>> queenMoves = expandBoard.getMoves(expandBoard.getBoard(), queenPosMove);
		
		for(ArrayList<Integer> queenMove: queenMoves) {
			//creating new board for simulation based off updated board
			GameBoard simulationBoard = new GameBoard(expandBoard);
			simulationBoard.updateBoard(queenPosMove, queenMove);
			
			ArrayList<ArrayList<Integer>> arrowShots = board.getMoves(board.getBoard(), queenMove);
			//handle arrowshot where queen WAS here as getMoves is overloaded for both and we don't want queen to stay still
			arrowShots.add(queenPosCur);
			for(ArrayList<Integer> arrowShot: arrowShots) {
				//cant shoot an arrow where the queen wants to move
				if(!queenMove.equals(arrowShot)) {
				//builds a list of both the queenMove and arrowShot
				List<Integer> move = Stream.of(queenMove,arrowShot)
						.flatMap(x -> x.stream())
						.collect(Collectors.toList());
				moves.add((ArrayList)move);
				}
			}
		}
		
		
		for(ArrayList<Integer> move: moves) {
			addChild(nodeToMove,move);
		}
	}
	
	private void backpropegate(Node child,int score) {
		
		while(child.getParent()!= null) {
			child.updateVists();
			child.updateScore(score);
			child=child.getParent();
		}
		//increment the root node
		child.updateScore(score);
		child.updateVists();
		
	}
	private int rollout(GameBoard board,Node nodeToMove, Node treeRootNode) {
		GameBoard rolloutBoard = new GameBoard(board);
		
		ArrayList<Integer> queenPosMove = new ArrayList<>(nodeToMove.getPosition().subList(0, 2));
		ArrayList<Integer> queenPosCur = new ArrayList<>(treeRootNode.getPosition());
		
		
		
		
		
		//possible arrow shots for moved queen
		ArrayList<ArrayList<Integer>> arrowShots = board.getMoves(rolloutBoard.getBoard(), queenPosMove);
		//try rolling out only using one player to move
	
		ArrayList<Integer>nextQueenPos;
		BoardTile[][] currentBoard = rolloutBoard.getBoard();
		//updating the board so the select queen is now moved to the current node not the root
		rolloutBoard.updateBoard(queenPosCur,queenPosMove,queenPosCur);
		//zero moves have been made
		int count = 0;
		//list of moves from the most recent queen move
		ArrayList<ArrayList<Integer>> nextMove = rolloutBoard.getMoves(currentBoard, queenPosMove);
		//while there are still moves
		while(nextMove.size()!=0 ) {
			
			//int rand = (int)Math.random() * nextMove.size();
			Random r = new Random();
			//picking random queen move
			int randMove = r.nextInt(nextMove.size());
			nextQueenPos = nextMove.get(randMove);
			//getting possible arrow shots for the corresponding random move
			ArrayList<ArrayList<Integer>> nextArrow = rolloutBoard.getMoves(currentBoard, nextQueenPos);
			nextArrow.add(nextQueenPos);
			
			int randArrow = r.nextInt(nextArrow.size());
			ArrayList<Integer> nextArrowPos = nextArrow.get(randArrow);
//			System.out.println(nextArrow + " arrow positions");
//			System.out.println(nextQueenPos + " queen position");
			//System.out.println(nextQueenPos.toString());
			rolloutBoard.updateBoard(queenPosMove, nextQueenPos, nextArrowPos);
			queenPosMove = nextQueenPos;
			count++;
			nextMove = rolloutBoard.getMoves(currentBoard, queenPosMove);
			nextArrow = rolloutBoard.getMoves(currentBoard, nextQueenPos);
		}
		
		//System.out.println(count);
		return count;
		
			
			
		}
	
	private Node addChild(Node parent, ArrayList<Integer> nodePosition) {
		Node node = new Node(parent,nodePosition);
		parent.getChildren().add(node);
		return node;
	}
	
	/*
	 * return the best move (queen move followed by arrow move) with score appended
	 */
	public ArrayList<Integer> getMove()
	{
		return bestMove;
	}
	
	public int getScore()
	{
		return bestScore;
	}
	
	public Queen getQueen()
	{
		return q;
	}
	
}











