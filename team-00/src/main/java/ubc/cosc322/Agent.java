package ubc.cosc322;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public class Agent extends GamePlayer{

	private GameClient gameClient = null;
	private String username = null;
	private String password = null;
	private GameRules game = null;
	private GameBoard board = null;
	private boolean isWhite;
	private List<Queen> queens = null;
	
	private Queen currentQueen = null;
	private ArrayList<Integer> nextMove = null;
	private int score = 0;
	
	String ourAmazon;
	String otherAmazon;
	
	
	public Agent(String username, String password) {
		this.username = username;
		this.password = password;
		gameClient = new GameClient(userName(), password, this);
	
	}
	

	
	
	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		//this is just to test ai is white vs human player
		/*
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		this.gameClient.joinRoom("Kentucky Lake");
		
	}
	





	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document.
				
    	    	switch(messageType) {
    	    	case GameMessage.GAME_ACTION_START:
    	    		System.out.println(this.userName());
    	    		System.out.println(AmazonsGameMessage.PLAYER_WHITE);
    	    		if((msgDetails.get(AmazonsGameMessage.PLAYER_WHITE)).equals(this.userName())){
    	    			System.out.println("The AI is white");
    	    			this.isWhite = true;
    	    			board.setAIColor(true);
    	    			queens = setTeam(board);
    	    			generateMove();
    	    		} else {
    	    			System.out.println("The AI is black");
    	    			this.isWhite = false;
    	    			board.setAIColor(false);
    	    			queens = setTeam(board);
    	    		}
    	    		
    	    		break;
//    	    		
//    	    		String black = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
//    	    		System.out.println(black);
    	    	case GameMessage.GAME_STATE_BOARD:
    	    		//System.out.println("were here "+ msgDetails.get(AmazonsGameMessage.GAME_STATE));
    	    		//game = new GameRules((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
    	    		board = new GameBoard((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
    	    		
    	    		break;
    	    	case GameMessage.GAME_ACTION_MOVE:
    	    		//Updating board based on opponents moves
    	    		board.updateBoard((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR), 
    	    				(ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT), 
    	    				(ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS));
    	    		generateMove();
    	    		break;
    	    	}
    	return true; 

	}
	
	private void generateMove()
	{
		//reset move and score every turn
		score = 0;
		currentQueen = null;
		nextMove = null;
		//Keep list of running montes to pull data from
		ArrayList<MCTS> monteList = new ArrayList<MCTS>();
		//need to reference threads
		ArrayList<Thread> threadsList = new ArrayList<Thread>();
		
		
		//Generate threads for queens
		for(Queen q : queens) {
			MCTS monte = new MCTS(board,q,this.isWhite);
			monteList.add(monte);
			Thread thread = new Thread(monte);
			threadsList.add(thread);
			System.out.println("new thread started");
			thread.start();
		}
		//Wait for threads to finish in parent thread (join syncs threads i.e. parent + children)
		for(Thread t : threadsList) {
			try {
				t.join();
				System.out.println("Thread returned");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // make parent wait
		}
		
		for(MCTS m : monteList)
		{
			//only one thread can access this at a time, probably not necessary
			tryUpdateMoveSet(m);
		}
		

		//ArrayList<Integer> nextMove = monte.run(board, current);
		ArrayList<Integer>qcur = currentQueen.getCurrentPos();
		if(!board.getBoard()[qcur.get(0)][qcur.get(1)].containsQueen()) {
			System.out.println("ILLEGAL MOVE!!!! NO QUEEN ON CURRENT TILE");
		}
		if(nextMove == null) {
			System.out.println("Game Over: OUT OF MOVES");
		}else {
			ArrayList<Integer> qmove = new ArrayList<>(nextMove.subList(0, 2));
			ArrayList<Integer> amove = new ArrayList<>(nextMove.subList(2, 4));
			System.out.println(qcur + "queen current");
			System.out.println(nextMove);
			System.out.println(qmove);
			System.out.println(amove);
			board.updateBoard(qcur, qmove, amove);
			gameClient.sendMoveMessage(qcur, qmove,amove);
		}
	}
	/*
	 * Threads shouldn't be able to access this but in case of any funny business its thread safe. 
	 */
	public synchronized void tryUpdateMoveSet(MCTS monte)
	{
		if(monte.getScore() >= score) {
			score = monte.getScore();
			//System.out.println(score);
			currentQueen = monte.getQueen();
			nextMove = monte.getMove();
			System.out.println(nextMove);
			System.out.println("Move updated");
		}
	}
	
	private List<Queen> setTeam(GameBoard board)
	{
		return (this.isWhite) ? board.getWQueens() : board.getBQueens();
	}

	@Override
	public String userName() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.getGameClient();
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		gameClient = new GameClient(userName(), password, this);
	}

}
