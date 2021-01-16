package finalProject1.states.gameState;

import java.awt.Point;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.Inputs;
import finalProject1.board.Board;
import finalProject1.entities.Entity;
import finalProject1.entities.Tower;
import finalProject1.entities.robots.Robot;
import finalProject1.network.NetworkData;
import finalProject1.states.GameEnd;
import finalProject1.states.State;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameState extends State{
	//this lets us send/receive data from the opponent and is needed to restart
	NetworkData opponentData;
	private Player player1;//you
	private Opponent player2;//the other player
	private GamePlayer activePlayer;//the player who's turn it is
	
	private FinalProject project;
	private Board board;//the board that the game is played on
	
	//whether you area a server or not, used to see whether you move first and to restart
	private boolean isServer;
	
	//this is added to the end turn button so that when it is clicked it goes to the next turn
	
	
	/**
	 * this is the state the game is in when it is actually being played.
	 * @param project - the project or main class that this gamestate belongs to.
	 */
	public GameState(FinalProject project, NetworkData opponent, boolean isServer) {
		System.out.println("starting:"+isServer);
		this.project=project;
		this.isServer=isServer;
		opponentData=opponent;				
	}
	
	@Override
	public void start() {
		board = new Board(project);	//creating the board
		//creating the players
		player1=new Player(board, project);
		player2=new Opponent(opponentData, project);
	
		//making you go first if you are the one who created the server
		if(isServer) {
			activePlayer=player1;
		}else {
			activePlayer=player2;
		}
		
		activePlayer.startTurn();
		//clearing the entity manager so things wont stay from past rounds
		Entity.getManager().reset();
		
		
		//creating the towers the players need to protect
		new Tower(project, 1, 0, 0);
		new Tower(project, 1, 0, 6);
		new Tower(project, 2, 6, 0);
		new Tower(project, 2, 6, 6);
	}
	
	/**
	 * this is called every frame and basically runs the entire game
	 */
	public void update() {
		///getting the mouses location and converting it to tiles
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		
		sendData();//sending any data that should be sent to the opponent
		//switching turns of the active player is done
		if(activePlayer.isDoneTurn()) {
			//giving both players money at the end of every turn
			player1.giveMoney(1);
			player2.giveMoney(1);
			//doing things if you just finished
			if(activePlayer instanceof Player) {
				player2.startTurn();//starting the opponents turn
				//reseting the opponents robots
				for(Entity i:Entity.getManager().getEntities(2)) {
					if(i instanceof Robot)
						((Robot) i).startTurn();
				}
				//making you robots unable to move
				for(Entity i:Entity.getManager().getEntities(1)) {
					if(i instanceof Robot)
						((Robot) i).endTurn();
				}
				//telling your opponent that you have finished your turn
				opponentData.sendData(NetworkData.ENDTURN+"");
				activePlayer=player2;//setting the active player to player 2
				
			//doing things if you opponent finished their turn
			}else if(activePlayer instanceof Opponent) {
				//this is basically the same as the previous part but with player 2 switched with player 1
				player1.startTurn();
				for(Entity i:Entity.getManager().getEntities(1)) {
					if(i instanceof Robot)
						((Robot) i).startTurn();
				}
				for(Entity i:Entity.getManager().getEntities(2)) {
					if(i instanceof Robot)
						((Robot) i).endTurn();
				}
				//telling the other player they can start their turn 
				opponentData.sendData(NetworkData.STARTTURN+"");
				activePlayer=player1;
			}
			
		}
			
		
		//highlighting the tile that is being hovered and showing information about anything on it
		board.reset();//reseting the board so tiles wont stay highlighted
		board.highlightTile(mouseLoc.x, mouseLoc.y);//highlighting the tile your mouse is hovering over
		
		//checking if player 1's towers are still there and showing the win screen if they arent
		boolean hasTowers=false;
		//checking the 1st players entities
		for(Entity i: Entity.getManager().getPlayer1()) {
			if(i instanceof Tower) {
				hasTowers=true;
				break;
			}
		}
		if(!hasTowers) {
			//do player 2 winning things
			State.setCurrentState(new GameEnd(project,opponentData,false));
		}
		//checking player 2s towers
		hasTowers=false;
		for(Entity i: Entity.getManager().getPlayer2()) {
			if(i instanceof Tower) {
				hasTowers=true;
				break;
			}
		}
		if(!hasTowers) {
			//do player 1 winning things
			State.setCurrentState(new GameEnd(project,opponentData,true));
		}
		//updating the entities
		Entity.getManager().update();
		//updating players
		player1.update();
		player2.update();
		
	}
	
	@Override
	public void end() {
		//clearing the screen when the game is over
		project.clear();
	}
	
	private void sendData() {
		//sending the data from the player to the opponent
		String data=player1.getDataToSend();
		if(data.length()>0) {//making sure there actually is data to send before sending it
			opponentData.sendData(data);
		}
	}
	
	//getters/setters
	public Board getBoard() {
		return board;
	}
}
