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
	
	public Agent(String username, String password) {
		this.username = username;
		this.password = password;
	
	}
	

	
	
	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		username = gameClient.getUserName();
		this.gameClient.joinRoom("Okanagan Lake");
		
	}
	





	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document.
		List currentPOS = null;
    	    	switch(messageType) {
    	    	case GameMessage.GAME_ACTION_START:
    	    		if((msgDetails.get(AmazonsGameMessage.PLAYER_WHITE).equals(this.username))){
    	    			System.out.println("The AI is white");
    	    			
    	    		} else {
    	    			System.out.println("The AI is black");
    	    		}
//    	    		
//    	    		String black = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
//    	    		System.out.println(black);
    	    	case GameMessage.GAME_STATE_BOARD:
    	    	
    	    		
    	    		break;
    	    	case GameMessage.GAME_ACTION_MOVE:
    	    		ArrayList<Integer> qcur = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
    	    		ArrayList<Integer> qnext = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
    	    		ArrayList<Integer> apos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
    	    		
    	    		gameClient.sendMoveMessage(qcur, qnext, apos);
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
		gameClient = new GameClient(username, password, this);
	}

}
