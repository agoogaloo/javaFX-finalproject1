package finalProject1;

import java.awt.Point;

import finalProject1.board.Board;
import finalProject1.entities.Assets;
import finalProject1.entities.Entity;
import finalProject1.entities.Tower;
import finalProject1.entities.robots.HeliBot;
import finalProject1.entities.robots.Robot;
import finalProject1.entities.robots.Tank;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameState {
	private final int PLAYERS=2;
	private int turn=1;
	private int money1=5,money2=5;
	
	private FinalProject project;
	private Board board;
	private Entity selected;
	
	private Text turnText = new Text(150*FinalProject.PIXEL_SCALE,10*FinalProject.PIXEL_SCALE,"player 1 turn"), 
			money1Text = new Text(50*FinalProject.PIXEL_SCALE,30," a"),money2Text = new Text(250*FinalProject.PIXEL_SCALE,30," b");
	private ImageView buyHeli = new ImageView(Assets.heliBot);
	private ImageView buyTank = new ImageView(Assets.tank);
	TurnState turnState = TurnState.IDLE;
	
	
	
	
	
	public GameState(FinalProject project) {
		this.project=project;
		turnText.setFill(Color.WHITESMOKE);
		turnText.setFont(Font.font("Comic Sans", 22));
		money1Text.setFill(Color.WHITESMOKE);
		money1Text.setFont(Font.font("Comic Sans", 22));
		money2Text.setFill(Color.WHITESMOKE);
		money2Text.setFont(Font.font("Comic Sans", 22));
		board = new Board(project);	
		
		new Tank(project,1,1,1);
		new HeliBot(project,1,1,5);
		new Tower(project, 1, 0, 0);
		new Tower(project, 1, 0, 6);
		
		new Tank(project,2,5,5).endTurn();
		new HeliBot(project,2,5,1).endTurn();
		new Tower(project, 2, 6, 0);
		new Tower(project, 2, 6, 6);
		
		buyHeli.setY(220*FinalProject.PIXEL_SCALE);
		buyTank.setY(220*FinalProject.PIXEL_SCALE);
		buyHeli.setX(150*FinalProject.PIXEL_SCALE);
		buyTank.setX(250*FinalProject.PIXEL_SCALE);
		
		project.add(turnText);
		project.add(money1Text);
		project.add(money2Text);
		project.add(buyHeli);
		project.add(buyTank);
		
		
	}
	public void update() {
		//updating the text boxen
		turnText.setText("player "+turn+" turn");
		money1Text.setText(money1+"");
		money2Text.setText(money2+"");
				
		///getting the mouses location
		
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		board.deHighlight();
		board.highlightTile(mouseLoc.x, mouseLoc.y);
		
		//highlighting the selected robot
		if(selected!=null) {
			board.highlightTile(selected.getX(),selected.getY());
		}
		
		if(Inputs.isClicked()) {
			onClick();
		}
		boolean hasTowers=false;
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
		switchTurns();		
		//updating the entities
		Entity.getManager().update();
		
	}
	
	private void buyBots() {
		int heliPrice=3,tankPrice=2;
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		if(mouseLoc.x<0||mouseLoc.y<0||mouseLoc.x>=Board.WIDTH|mouseLoc.y>=Board.HEIGHT) {
			turnState=TurnState.IDLE;
			return;
		}
		switch (turnState) {
		case BUYHELI:
			if(turn==1) {
				if(money1>=heliPrice) {
					new HeliBot(project, 1,mouseLoc.x,mouseLoc.y).endTurn();
					money1-=heliPrice;
				}
			}else if(turn==2) {
				if(money1>=tankPrice) {
					new HeliBot(project, 2,mouseLoc.x,mouseLoc.y).endTurn();
					money2-=heliPrice;
				}
			}
			break;
		case BUYTANK:
			if(turn==1) {
				if(money1>=tankPrice) {
					new Tank(project, 1,mouseLoc.x,mouseLoc.y).endTurn();
					money1-=tankPrice;
				}
			}else if(turn==2) {
				if(money1>=tankPrice) {
					new Tank(project, 2,mouseLoc.x,mouseLoc.y).endTurn();
					money2-=tankPrice;
				}
			}
			break;

			
		default:
			return;
		}
		turnState=TurnState.IDLE;
	}
	/**
	 * this checks if the turn is over and goes to the next turn if it should switch
	 */
	private void onClick() {
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		buyBots();
		if(buyHeli.isPressed())
			turnState=TurnState.BUYHELI;
		else if(buyTank.isPressed())
			turnState=TurnState.BUYTANK;
		else if(Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y) instanceof Robot) {
			selected=Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);	
			turnState = TurnState.ROBOTSELECT;
		}else {
			turnState=TurnState.IDLE;
		}
		
		
		if(selected instanceof Robot) {
			Robot robot=(Robot) selected;
			if(robot.canMove()) {
				if(!robot.move(mouseLoc.x, mouseLoc.y)) {
					selected=null;
				}
			}else if (robot.canAttack()) {
				if(!robot.attack(mouseLoc.x, mouseLoc.y)) {
					selected=null;
					
				}
			}else { 
				selected=Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);	
			}
				
			
		}else{
			selected=Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);	
		}
	}
	
	private void switchTurns() {
		for(Entity i:Entity.getManager().getEntities(turn)) {
			if(i instanceof Robot&&((Robot) i).canAttack()) {
				return;
			}
		}
		turn++;
		money1++;
		money2++;
		if(turn>PLAYERS) {
			turn=1;
		}
		
		for(Entity i:Entity.getManager().getEntities(turn)) {
			if(i instanceof Robot)
				((Robot) i).startTurn();
		}
	}
	
	//getters
	public Board getBoard() {
		return board;
	}
	
}
