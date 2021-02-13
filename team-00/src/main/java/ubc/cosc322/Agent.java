package ubc.cosc322;

import java.util.ArrayList;
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
	
		this.gameClient.joinRoom("Kootenay Lake");
		
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
    	    		if((msgDetails.get(AmazonsGameMessage.PLAYER_WHITE)).equals(this.userName())){
    	    			System.out.println("The AI is white");
    	    			board.setAIColor(true);
    	    		} else {
    	    			System.out.println("The AI is black");
    	    			board.setAIColor(false);
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
    	    		board.updateBoard((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR), (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT), (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS));
    	    		
    	    		//ArrayList<Integer> q_orig = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
    	    		//System.out.println("QueenStart: " + q_orig.get(0) + ", " + q_orig.get(1));
    	    		//ArrayList<Integer> q_next = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
    	    		//System.out.println("QMoved: " + q_next.get(0) + ", " + q_next.get(1));
    	    		//ArrayList<Integer> a_pos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
    	    		//System.out.println("ArrowAt: " + a_pos.get(0) + ", " + a_pos.get(1));
//    	    		qcur.set(0, 10);
//    	    		qcur.set(1, 7);
//    	    		qnext.set(0, 9);
//    	    		qnext.set(1, 2);
//    	    		apos.set(0, 3);
//    	    		apos.set(1, 3);
    	    		//TODO implement this logic with new classes
    	    		//ArrayList<Integer> qcur =GameRules.findCurrent();
    	    		//ArrayList<Integer> qnext =GameRules.makeMove();
    	    		
    	    		//gameClient.sendMoveMessage(qcur, qnext, qcur);
    	    	
    	    		break;
    	    	}
    	return true; 

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
