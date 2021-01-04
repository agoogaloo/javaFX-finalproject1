package gameState;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.Inputs;
import finalProject1.TurnState;
import finalProject1.board.Board;
import finalProject1.entities.Entity;
import finalProject1.entities.Tower;
import finalProject1.entities.robots.HeliBot;
import finalProject1.entities.robots.Robot;
import finalProject1.entities.robots.Tank;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameState {
	
	private final int PLAYERS=2;//how many players there are 
	private Player player1, player2;//the 2 players
	private Player activePlayer;//the player who's turn it is
	
	private FinalProject project;
	private Board board;//the board that the game is played on
	private Robot selected;//this is the robot that has been clicked on to move or attack
	
	//this shows who's turn it is
	private Text turnText = new Text(158*FinalProject.PIXEL_SCALE,15*FinalProject.PIXEL_SCALE,"Player 1 turn");
	
	private ImageView buyCard = new ImageView(Assets.cardBack);//the button to buy a new card
	//an arraylist holding the deck that you draw random robots from. it isnt modifiable yet
	private ArrayList<Integer> deck = new ArrayList<>(Arrays.asList(Tank.ID,HeliBot.ID));
	//what state the turn is in (whether a robot has been selected to move, if its just waiting, or placing a robot)
	TurnState turnState = TurnState.IDLE;
	
	/**
	 * this is the state the game is in when it is being played. currently there arent other 
	 * states to switch to,but if i made was a main menu or pause screen,i would make it into its own state but i dont
	 * @param project - the project or main class that this gamestate belongs to.
	 */
	public GameState(FinalProject project) {
		this.project=project;
		//setting the text font and colour
		turnText.setFill(Color.WHITE);
		turnText.setFont(Assets.font);
		
		board = new Board(project);	//creating the board
		//creating the players and making player 1 go 1st
		player1=new Player(1, project);
		player2=new Player(2, project);
		activePlayer=player1;
		
		//creating the towers the players need to protect
		new Tower(project, 1, 0, 0);
		new Tower(project, 1, 0, 6);
		new Tower(project, 2, 6, 0);
		new Tower(project, 2, 6, 6);
		
		//setting the location of the deck to buy cards from
		buyCard.setX(209*FinalProject.PIXEL_SCALE);
		buyCard.setY(4*FinalProject.PIXEL_SCALE);
		
		//adding everything to the screen that needs to be added
		project.add(turnText);
		project.add(buyCard);		
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
		}
		//switching/checking if it should go to the next turn
		switchTurns();		
		//updating the entities
		Entity.getManager().update();
		//updating players
		player1.update();
		player2.update();
		
		//if the player is buying a robot and still needs to place it it should go switch to the buybot state
		if(activePlayer.getSelectedBot()!=-1) {
			turnState=TurnState.BUYBOT;
		}
	}
	
	private void buyBots(Point mouseLoc) {
		if((activePlayer.PLAYERNUM==1&&mouseLoc.x>=0&&mouseLoc.y>=0&&mouseLoc.x<=1&&mouseLoc.y<Board.HEIGHT)
				||(activePlayer.PLAYERNUM==2&&mouseLoc.x>=Board.WIDTH-2&&
				mouseLoc.y>=0&&mouseLoc.x<Board.WIDTH&&mouseLoc.y<Board.HEIGHT)
				&&Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y)==null) {
			
			
			switch(activePlayer.getSelectedBot()) {
			case Tank.ID:
				new Tank(project, activePlayer.PLAYERNUM,mouseLoc.x,mouseLoc.y).endTurn();
				break;
			case HeliBot.ID:
				new HeliBot(project, activePlayer.PLAYERNUM,mouseLoc.x,mouseLoc.y).endTurn();
				break;
			}
			turnState=TurnState.IDLE;
			activePlayer.placeBot();
		}
		return;
		
		
	}
	
	private void showInfo(Entity e) {
		for(Entity i:Entity.getManager().getEntities(1)) {
			i.hideInfo();
		}
		for(Entity i:Entity.getManager().getEntities(2)) {
			i.hideInfo();
		}
		if(e!=null) {
			e.showInfo();
		}	
	}
	
	/**
	 * this checks if the turn is over and goes to the next turn if it should switch
	 */
	private void onClick() {
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		if(turnState==TurnState.BUYBOT) {
			buyBots(mouseLoc);
			return;
		}
		
		if(turnState==TurnState.ROBOTSELECT) {
			if(selected.canMove()) {
				if(!selected.move(mouseLoc.x, mouseLoc.y)) {
					selected=null;
					turnState=TurnState.IDLE;					
				}
			}else if (selected.canAttack()) {
				if(!selected.attack(mouseLoc.x, mouseLoc.y)) {
					selected=null;
					turnState=TurnState.IDLE;
				}
			}else { 
				selected=null;
				turnState=TurnState.IDLE;	
			}
		}else if(Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y) instanceof Robot) {
			selected=(Robot) Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);	
			turnState = TurnState.ROBOTSELECT;
		}
		if(buyCard.isPressed()) {
			activePlayer.buyCard(deck.get(ThreadLocalRandom.current().nextInt(0, deck.size())));
		}
	}
	
	private void switchTurns() {
		for(Entity i:Entity.getManager().getEntities(activePlayer.getPlayerNum())) {
			if(i instanceof Robot&&((Robot) i).canAttack()) {
				return;
			}
		}
		if(activePlayer.getPlayerNum()==1) {
			activePlayer=player2;
		}else {
			activePlayer=player1;
		}
		player1.addMoney(1);
		player2.addMoney(1);
		
		
		for(Entity i:Entity.getManager().getEntities(activePlayer.getPlayerNum())) {
			if(i instanceof Robot)
				((Robot) i).startTurn();
		}
	}
	
	//getters
	public Board getBoard() {
		return board;
	}
	
	public void setTurnState(TurnState turnState) {
		this.turnState = turnState;
	}
}
