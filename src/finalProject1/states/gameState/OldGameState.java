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

public class OldGameState extends State{
	
	private OldPlayer player1, player2;//the 2 players
	private OldPlayer activePlayer;//the player who's turn it is
	
	private FinalProject project;
	private Board board;//the board that the game is played on
	private Robot selected;//this is the robot that has been clicked on to move or attack
	
	//ui stuff
	private Text infoText = new Text(113*FinalProject.PIXEL_SCALE,230*FinalProject.PIXEL_SCALE,"");
	//this shows who's turn it is
	private Text turnText = new Text(158*FinalProject.PIXEL_SCALE,15*FinalProject.PIXEL_SCALE,"Player 1 turn");
	private ImageView buyCard = new ImageView(Assets.cardBack);//the button to buy a new card
	private Group endTurn = new Group();//this is the button on the bottom that lets you end your turn
	
	//an arraylist holding the deck that you draw random robots from. it isnt modifiable yet
	private ArrayList<Integer> deck = new ArrayList<>(Arrays.asList(Tank.ID,HeliBot.ID, TreadBot.ID, Turret.ID));
	//what state the turn is in (whether a robot has been selected to move, if its just waiting, or placing a robot)
	TurnState turnState = TurnState.IDLE;
	
	//this is added to the end turn button so that when it is clicked it goes to the next turn
	private EventHandler<MouseEvent> endTurnClicked=new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent arg0) {
			//switching turns
			for(Entity i:Entity.getManager().getEntities(activePlayer.getPlayerNum())) {
				if(i instanceof Robot)
					((Robot) i).endTurn();
			}
			if(activePlayer.getPlayerNum()==1) {
				activePlayer=player2;
			}else {
				activePlayer=player1;
			}
			//giving both players money at the end of each turn
			player1.addMoney(1);
			player2.addMoney(1);
			
			//making all the robots start their turn so they can move and attack again
			for(Entity i:Entity.getManager().getEntities(activePlayer.getPlayerNum())) {
				if(i instanceof Robot)
					((Robot) i).startTurn();
			}
		}
	};
	
	/**
	 * this is the state the game is in when it is actually being played.
	 * @param project - the project or main class that this gamestate belongs to.
	 */
	public OldGameState(FinalProject project, NetworkData opponent) {
		this.project=project;
		//setting the text font and colour
		turnText.setFill(Color.WHITE);
		turnText.setFont(Assets.font);
		infoText.setFont(Assets.font);
		infoText.setFill(Color.WHITE);
		
		
		//the text for the end turn button
		Text endTurnText=new Text(7*FinalProject.PIXEL_SCALE, 12*FinalProject.PIXEL_SCALE, "END TURN");
		endTurnText.setFill(Color.WHITE);
		endTurnText.setFont(Assets.font);
		
		//moving the endturn button to the right spot and adding the graphics to it
		endTurn.setTranslateX(200*FinalProject.PIXEL_SCALE);
		endTurn.setTranslateY(230*FinalProject.PIXEL_SCALE);
		endTurn.getChildren().add(new Rectangle(50*FinalProject.PIXEL_SCALE,
				20*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		endTurn.getChildren().add(endTurnText);
		endTurn.setOnMouseClicked(endTurnClicked);//adding the mouse event so it ends the turn when its clicked
		
		//setting the location of the deck to buy cards from
		buyCard.setX(209*FinalProject.PIXEL_SCALE);
		buyCard.setY(4*FinalProject.PIXEL_SCALE);
		
		//adding everything to the screen that needs to be added
		
	}
	
	@Override
	public void start() {
		board = new Board(project);	//creating the board
		//creating the players and making player 1 go 1st
		player1=new OldPlayer(1, project);
		player2=new OldPlayer(2, project);
		activePlayer=player1;
		
		//creating the towers the players need to protect
		new Tower(project, 1, 0, 0);
		new Tower(project, 1, 0, 6);
		new Tower(project, 2, 6, 0);
		new Tower(project, 2, 6, 6);
		
		project.add(infoText);
		project.add(turnText);
		project.add(buyCard);
		project.add(endTurn);
	}
	
	/**
	 * this is called every frame and basically runs the entire game
	 */
	public void update() {
		///getting the mouses location and converting it to tiles
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		
		//showing who's turn it is
		if(activePlayer.getPlayerNum()==1) {
			turnText.setX(313*FinalProject.PIXEL_SCALE);
			turnText.setText("WAITING FOR \nPLAYER 1 TO \nFINISH THEIR TURN");	
		}else if(activePlayer.getPlayerNum()==2) {
			turnText.setX(64*FinalProject.PIXEL_SCALE);
			turnText.setText("WAITING FOR \nPLAYER 2 TO \nFINISH THEIR TURN");	
		}	
		
		//highlighting the tile that is being hovered and showing information about anything on it
		board.reset();//reseting the board so tiles wont stay highlighted
		board.highlightTile(mouseLoc.x, mouseLoc.y);//highlighting
		//showing the entities info if there is one on that tile
		showInfo(Entity.getManager().getEntity(mouseLoc.x,mouseLoc.y));
		
		//doing things if a robot has been selected
		if(turnState==TurnState.ROBOTSELECT) {
			//showing that the robot has been selected by changing its tile
			board.selectTile(selected.getX(),selected.getY());
			
			//showing the spaces it can attack/move
			if(((Robot) selected).canMove()) {//showing movable tiles if it can move by highlighting them
				for(Point i:((Robot) selected).movableTiles()) {
					board.highlightTile(i.x, i.y);
				}
			//if it cant move but can still attack it will show the attackable tiles
			}else if(((Robot) selected).canAttack()) {
				for(Point i:((Robot) selected).attackableTiles()) {
					board.highlightTile(i.x, i.y);
				}
			}
		}
		//doing things that happen when the mouse is clicked
		if(Inputs.isClicked()) {
			onClick();
		}
		
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
			turnText.setText("player 2 wins");
			//State.setCurrentState(new GameEnd(project));
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
			turnText.setText("player 1 wins");
			//State.setCurrentState(new GameEnd(project,1));
		}
		//updating the entities
		Entity.getManager().update();
		//updating players
		player1.update(turnState);
		player2.update(turnState);
		
		//if the player is buying a robot and still needs to place it it should go switch to the buybot state
		if(activePlayer.getSelectedBot()!=-1) {
			turnState=TurnState.BUYBOT;
		}
	}
	
	@Override
	public void end() {
		project.clear();
	}
	
	/**
	 * this runs when the player has selected where they want to place their robot
	 * @param mouseLoc - the location of the mouse in tiles
	 */
	private void buyBots(Point mouseLoc) {
		//making sure the selected tile is empty and is on the first 2 rows of their side before it can be placed
		if((activePlayer.PLAYERNUM==1&&mouseLoc.x>=0&&mouseLoc.y>=0&&mouseLoc.x<=1&&mouseLoc.y<Board.HEIGHT)
				||(activePlayer.PLAYERNUM==2&&mouseLoc.x>=Board.WIDTH-2&&
				mouseLoc.y>=0&&mouseLoc.x<Board.WIDTH&&mouseLoc.y<Board.HEIGHT)
				&&Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y)==null) {
			
			//placing a different robot depending on which card the player wants to place
			switch(activePlayer.getSelectedBot()) {
			case Tank.ID:
				//ending the robots turn after they place it so that they cant move the turn they are placed
				new Tank(project, activePlayer.PLAYERNUM,mouseLoc.x,mouseLoc.y).endTurn();
				break;
			case HeliBot.ID:
				new HeliBot(project, activePlayer.PLAYERNUM,mouseLoc.x,mouseLoc.y).endTurn();
				break;
			case Turret.ID:
				new Turret(project, activePlayer.PLAYERNUM,mouseLoc.x,mouseLoc.y).endTurn();
				break;
			case TreadBot.ID:
				new TreadBot(project, activePlayer.PLAYERNUM,mouseLoc.x,mouseLoc.y).endTurn();
				break;
			}
			//returning the state back to idle if the robot could be placed
			turnState=TurnState.IDLE;
			//letting the player class know the robot has been placed
			activePlayer.placeBot();
		}
		
	}
	
	//this shows the info for the entity that is being hovered over
	private void showInfo(Entity e) {
		if(e!=null) {
			infoText.setText(e.getDescription().toUpperCase());
			endTurn.setVisible(false);
		}else if(buyCard.isHover()) {
			infoText.setText("BUY A NEW CARD FOR $1");
			endTurn.setVisible(false);
		}else {
			infoText.setText("");
			endTurn.setVisible(true);
		}
		
	}
	
	/**
	 *this is run whenever the mouse is clicked. most actions only happen when the mouse is clicked so 
	 *this is seperate from the update method to make it easier to read
	 */
	private void onClick() {
		//getting the mouse's location in tiles
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		//if the they are buying a robot, the next place they click is where the robot wil be placed
		if(turnState==TurnState.BUYBOT) {
			buyBots(mouseLoc);
			//you shouldnt be able to do anything else in the buybot state because it could 
			//mess things up so we exit the method
			return;
		}
		//doing thins if a robot has been selected to move/attack
		if(turnState==TurnState.ROBOTSELECT) {
			turnState=TurnState.IDLE;
			//moving the robot if the robot is able to move
			if(selected.canMove()) {
				if(!selected.move(mouseLoc.x, mouseLoc.y)) {
					//unselecting the robot if they select someplace that they cant move
					selected=null;
										
				}
			//attacking if the robot has already moved and can still attack
			}else if (selected.canAttack()) {
				if(!selected.attack(mouseLoc.x, mouseLoc.y)) {
					selected=null;
					turnState=TurnState.IDLE;
				}
			//unslecting the robot if it has already moved and attacked
			}else { 
				selected=null;
				turnState=TurnState.IDLE;	
			}
		//if a robot isnt selected and they click on a robot, is should be selected
		}else if(Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y) instanceof Robot) {
			//selecting the robot
			selected=(Robot) Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);	
			turnState = TurnState.ROBOTSELECT;
		}
		//buying a card if they have clicked on the buy a card button
		if(buyCard.isPressed()) {
			activePlayer.buyCard(deck.get(ThreadLocalRandom.current().nextInt(0, deck.size())));
		}
	}
	
	//getters/setters
	public Board getBoard() {
		return board;
	}
}
