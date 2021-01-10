package finalProject1.states.gameState;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.Inputs;
import finalProject1.board.Board;
import finalProject1.entities.Entity;
import finalProject1.entities.Tower;
import finalProject1.entities.robots.HeliBot;
import finalProject1.entities.robots.Robot;
import finalProject1.entities.robots.Tank;
import finalProject1.entities.robots.TreadBot;
import finalProject1.entities.robots.Turret;
import finalProject1.network.NetworkData;
import finalProject1.states.GameEnd;
import finalProject1.states.State;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameState extends State{
	
	NetworkData opponentData;
	private Player player1; 
	private Opponent player2;//the 2 players
	private GamePlayer activePlayer;//the player who's turn it is
	
	private FinalProject project;
	private Board board;//the board that the game is played on
	
	//ui stuff
	private Text infoText = new Text(113*FinalProject.PIXEL_SCALE,230*FinalProject.PIXEL_SCALE,"");
	
	private boolean isStarting;
	
	//this is added to the end turn button so that when it is clicked it goes to the next turn
	
	
	/**
	 * this is the state the game is in when it is actually being played.
	 * @param project - the project or main class that this gamestate belongs to.
	 */
	public GameState(FinalProject project, NetworkData opponent, boolean isStarting) {
		System.out.println("starting:"+isStarting);
		this.project=project;
		this.isStarting=isStarting;
		opponentData=opponent;
		
		//setting the text font and colour
		infoText.setFont(Assets.font);
		infoText.setFill(Color.WHITE);
		
		
		//the text for the end turn button
		Text endTurnText=new Text(7*FinalProject.PIXEL_SCALE, 12*FinalProject.PIXEL_SCALE, "END TURN");
		endTurnText.setFill(Color.WHITE);
		endTurnText.setFont(Assets.font);
		
		
	}
	
	@Override
	public void start() {
		board = new Board(project);	//creating the board
		//creating the players and making player 1 go 1st
		player1=new Player(board, project);
		player2=new Opponent(opponentData, project);
	
		if(isStarting) {
			activePlayer=player1;
		}else {
			activePlayer=player2;
		}
		activePlayer.startTurn();
		
		
		//creating the towers the players need to protect
		new Tower(project, 1, 0, 0);
		new Tower(project, 1, 0, 6);
		new Tower(project, 2, 6, 0);
		new Tower(project, 2, 6, 6);
		
		project.add(infoText);
		
	}
	
	/**
	 * this is called every frame and basically runs the entire game
	 */
	public void update() {
		///getting the mouses location and converting it to tiles
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		
		sendData();
		if(activePlayer.isDoneTurn()) {
			System.out.println("player 1:"+player1.isDoneTurn()+"player2:"+player2.isDoneTurn());
			player1.giveMoney(1);
			player2.giveMoney(1);
			if(activePlayer instanceof Player) {
				player2.startTurn();
				for(Entity i:Entity.getManager().getEntities(2)) {
					if(i instanceof Robot)
						((Robot) i).startTurn();
				}
				for(Entity i:Entity.getManager().getEntities(1)) {
					if(i instanceof Robot)
						((Robot) i).endTurn();
				}
				opponentData.sendData(NetworkData.ENDTURN+"");
				activePlayer=player2;
			}else if(activePlayer instanceof Opponent) {
				player1.startTurn();
				for(Entity i:Entity.getManager().getEntities(1)) {
					if(i instanceof Robot)
						((Robot) i).startTurn();
				}
				for(Entity i:Entity.getManager().getEntities(2)) {
					if(i instanceof Robot)
						((Robot) i).endTurn();
				}
				opponentData.sendData(NetworkData.STARTTURN+"");
				activePlayer=player1;
			}
			
		}
			
		
		
		//highlighting the tile that is being hovered and showing information about anything on it
		board.reset();//reseting the board so tiles wont stay highlighted
		board.highlightTile(mouseLoc.x, mouseLoc.y);//highlighting
		//showing the entities info if there is one on that tile
		showInfo(Entity.getManager().getEntity(mouseLoc.x,mouseLoc.y));
		
		
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
			//infoText.setText("player 2 wins");
			State.setCurrentState(new GameEnd(project,2));
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
			//infoText.setText("player 1 wins");
			State.setCurrentState(new GameEnd(project,1));
		}
		//updating the entities
		Entity.getManager().update();
		//updating players
		player1.update();
		player2.update();
		
	}
	
	@Override
	public void end() {
		project.clear();
	}
	
	private void sendData() {
		String data=player1.getDataToSend();
		if(data.length()>0) {
			opponentData.sendData(data);
		}
	}
	
	//this shows the info for the entity that is being hovered over
	private void showInfo(Entity e) {
		if(e!=null) {
			infoText.setText(e.getDescription().toUpperCase());
			//endTurn.setVisible(false);
		}else {
			infoText.setText("");
			//endTurn.setVisible(true);
		}
	}
	
	//getters/setters
	public Board getBoard() {
		return board;
	}
}
