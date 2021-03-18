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
	boolean isWhite;
	//shoudl take a copy of the game board
	
	public MCTS(GameBoard b,Queen q,boolean isWhite) 
	{
		this.b = b;
		this.q = q;
		this.isWhite = isWhite;
	}
	
	@Override
	public void run() {
	GameBoard board = new GameBoard(b);
	Queen queen = new Queen(q);
	
	ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
	ArrayList<Integer> queenPos = queen.getCurrentPos();
	
	ArrayList<ArrayList<Integer>> queenmoves = board.getMoves(board.getBoard(), queenPos);
	
	System.out.println(queenPos + " quuen position");
	Node treeRootNode = new Node(null,queenPos);

	
	
	Node current = treeRootNode;
	long startTime = System.currentTimeMillis();
	
	while(System.currentTimeMillis()-startTime<10000) {
		//System.out.println(current.getPosition());
		//System.out.println("checking isLeaf");
		
		if(current.isLeaf()) {
			if(current.getParent()==null) {
				ArrayList<Integer> queenPosCur = new ArrayList<>(treeRootNode.getPosition());
				
				//this is clone of original board - moving queen from current pos to next theoretical position
				
				//getting all moves from that new position
				ArrayList<ArrayList<Integer>> queenMoves = board.getMoves(board.getBoard(), queenPosCur);
				
				for(ArrayList<Integer> queenMove: queenMoves) {
					//creating new board for simulation based off updated board
					GameBoard simulationBoard = new GameBoard(board);
					simulationBoard.updateBoard(queenPosCur, queenMove);
					
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
					addChild(treeRootNode,move);
				
}
				
			}
			else if(current.getVisits()==0) {
				//System.out.println("doing rollout");
				int score = rollout(board,current,treeRootNode,isWhite);
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
	System.out.println("Tree root node:" + treeRootNode.getScore());
	ArrayList<Integer> potentialMove = null;
	int best =0;
	for(Node child: treeRootNode.getChildren()) {
		potentialMove = child.getPosition(); // this is a workaround to unknown bug where child ends up with illegal position
		System.out.println("visits: "+child.getVisits()+" UCB1: "+ getUCB1(child)+ " for node: "+child.getPosition() + " score: " + child.getScore());
		if(child.getScore()>best && !b.getBoard()[potentialMove.get(0)][potentialMove.get(1)].containsArrow()) {
			best = child.getScore();
			System.out.println(child.getVisits());
			potentialMove = child.getPosition();
			System.out.println(potentialMove + " from inside");
			
		}
			
	}
	//printTree(treeRootNode," ");
	System.out.println(potentialMove + " from outside");
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
		return score + Math.sqrt((Math.log(parentVisits)/visits));
	}
	
	private Node getNextNode(Node parentNode) {
		List<Node> children = parentNode.getChildren();
		double score = children.get(0).getScore();
		Node nextNode = children.get(0);
		//System.out.println(children);
		//looping through nodes and will return the position of the node(acting as unique identifier for the node)
		for(Node child: children) {
			double UCB1 = getUCB1(child);
			
			//System.out.println("visits: "+child.getVisits()+" UCB1: "+ getUCB1(child)+ " for node: "+child.getPosition() + " score: " + child.getScore());
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
		ArrayList<Integer> arrowPos = new ArrayList<>(nodeToMove.getPosition().subList(2, 4));
		ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
		//this is clone of original board - moving queen from current pos to next theoretical position
		expandBoard.updateBoard(queenPosCur, queenPosMove,arrowPos);
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
	private int rollout(GameBoard board,Node nodeToMove, Node treeRootNode,boolean isWhite) {
		
		
		List<Queen> theirQueens = new ArrayList<>();
		
		if(isWhite) {
			theirQueens = board.getBQueens();
		}else {
			theirQueens = board.getWQueens();
		}
		int win =0;
		
		for(int i = 0;i<10;i++) {
		GameBoard rolloutBoard = new GameBoard(board);
		//get our first move
		ArrayList<Integer> queenPosMove = new ArrayList<>(nodeToMove.getPosition().subList(0,2));
		ArrayList<Integer> queenPosCur = new ArrayList<>(treeRootNode.getPosition());
		ArrayList<Integer> arrowPos = new ArrayList<>(nodeToMove.getPosition().subList(2, 4));
		//update rolloutBoard with our move
		rolloutBoard.updateBoard(queenPosCur, queenPosMove, arrowPos);
		queenPosCur = queenPosMove;
		ArrayList<ArrayList<Integer>> ourQueenMoves = rolloutBoard.getMoves(rolloutBoard.getBoard(), queenPosCur);
		ArrayList<Integer> theirQueenPosCur =  theirQueens.get(0).getCurrentPos();
		ArrayList<ArrayList<Integer>> theirQueenMoves = rolloutBoard.getMoves(rolloutBoard.getBoard(),theirQueenPosCur);
		GameBoard simulationBoard = new GameBoard(rolloutBoard);
		int count =0;
		while(!ourQueenMoves.isEmpty() && !theirQueenMoves.isEmpty()) {
			Random r = new Random();
			if(count%2==0) {
				//their move
				ArrayList<Integer> theirNextQueenPos;
				ArrayList<ArrayList<Integer>> theirNextArrowShots;
				ArrayList<Integer> theirNextArrowPos;
				
				int randMove = r.nextInt(theirQueenMoves.size());
				//getting random move from possible moves
				theirNextQueenPos = theirQueenMoves.get(randMove);
				//getting arrow shots calculated from next move
				theirNextArrowShots = simulationBoard.getMoves(simulationBoard.getBoard(), theirNextQueenPos);
				//adding queens current position as possible arrow shot
				theirNextArrowShots.add(theirQueenPosCur);
				int randArrow = r.nextInt(theirNextArrowShots.size());
				//getting random arrow shot
				theirNextArrowPos = theirNextArrowShots.get(randArrow);
				//updating board moving their queen and making an arrow shot
				simulationBoard.updateBoard(theirQueenPosCur, theirNextQueenPos, theirNextArrowPos);
				//setting their current queen position to the moved position
				theirQueenPosCur = theirNextQueenPos;
				//generating moves from their current position
				ourQueenMoves = simulationBoard.getMoves(simulationBoard.getBoard(), queenPosCur);
			}else {
				//our move
				ArrayList<Integer> ourNextQueenPos;
				ArrayList<ArrayList<Integer>> ourNextArrowShots;
				ArrayList<Integer> ourNextArrowPos;
				int randMove = r.nextInt(ourQueenMoves.size());
				//getting random move from list of our possible moves
				ourNextQueenPos = ourQueenMoves.get(randMove);
				//getting possible arrow shots from new queen position
				ourNextArrowShots = simulationBoard.getMoves(simulationBoard.getBoard(), ourNextQueenPos);
				//adding current queen position as possible arrow shot
				ourNextArrowShots.add(queenPosCur);
				int randArrow = r.nextInt(ourNextArrowShots.size());
				//picking random arrow shot
				ourNextArrowPos = ourNextArrowShots.get(randArrow);
				//updating simulation board with our move
				simulationBoard.updateBoard(queenPosCur, ourNextQueenPos, ourNextArrowPos);
				//setting queens current position to the latest move
				queenPosCur = ourNextQueenPos;
				//getting list of possible moves
				theirQueenMoves = simulationBoard.getMoves(simulationBoard.getBoard(), theirQueenPosCur);
				
			}
			
			count++;
		}
		if(ourQueenMoves.isEmpty())
			win++;
		
		}
		
		
		return win;
		
			
			
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











