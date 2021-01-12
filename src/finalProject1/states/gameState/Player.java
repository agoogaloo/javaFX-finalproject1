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
import finalProject1.entities.robots.HeliBot;
import finalProject1.entities.robots.Robot;
import finalProject1.entities.robots.Tank;
import finalProject1.entities.robots.TreadBot;
import finalProject1.entities.robots.Turret;
import finalProject1.network.NetworkData;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Player extends GamePlayer{

	private TurnState turnState=TurnState.IDLE;
	private Board board;
	private Robot selectedBot=null;
	private int selectedCard=-1;
	private String dataToSend="";
	
	private Text turnText = new Text(120*FinalProject.PIXEL_SCALE,150*FinalProject.PIXEL_SCALE,"WAITING FOR YOUR OPPONENT");
	private Text infoText = new Text(113*FinalProject.PIXEL_SCALE,230*FinalProject.PIXEL_SCALE,"");
	
	private ArrayList<Integer> deck = new ArrayList<>(Arrays.asList(Tank.ID,HeliBot.ID, TreadBot.ID, Turret.ID));
	
	private ArrayList<Integer> hand = new ArrayList<>();//the id of the robots in their hand
	private ArrayList<Group> handPics = new ArrayList<>();//the pictures of the cards
	private ImageView buyCard = new ImageView(Assets.deck);//the button to buy a new card
	private ImageView selecttionArrow = new ImageView(Assets.arrow);
	
	private Group endTurn=new Group();
	
	public Player(Board board,FinalProject project) {
		super(project);
		this.board=board;
		playerNum=1;
		buyCard.setX(208*FinalProject.PIXEL_SCALE);
		buyCard.setY(3*FinalProject.PIXEL_SCALE);
		
		selecttionArrow.setX(10*FinalProject.PIXEL_SCALE);
		selecttionArrow.setVisible(false);
		
		
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
		
		turnText.setFont(Assets.boldfont);
		turnText.setFill(Color.WHITE);
		turnText.toFront();
		
		
		infoText.setFont(Assets.font);
		infoText.setFill(Color.WHITE);
		
		
		project.add(turnText);
		project.add(infoText);
		project.add(endTurn);
		project.add(buyCard);
		project.add(selecttionArrow);
		
		money+=2;
		buyCard();
		buyCard();
		
		
		
		
	}
	
	@Override
	public void update() {
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		//updating the textboxen
		
		if(doneTurn) {
			turnText.setVisible(true);
		}else {
			turnText.setVisible(false);
		}
		showInfo(mouseLoc);
		moneyText.setText("$"+money);
		
		if(Inputs.isClicked()&&!doneTurn) {
			onClick(mouseLoc);
		}
		
		//correcting the vertical position of the cards
		for(int i=hand.size()-1;i>=0;i--) {//i loop backwards so the cards on top will be checked first		
			//setting the card to be in the right place case other cards have been played
			handPics.get(i).setTranslateY((i*35+12)*FinalProject.PIXEL_SCALE);
		}
		if(turnState==TurnState.ROBOTSELECT) {
			highlightSelection();
		}
		
	}
	
	private void onClick(Point mouseLoc) {
		if(turnState==TurnState.BUYBOT) {
			buyBot(mouseLoc,hand.get(selectedCard));
			//making sure you cant do anytihng other than place a card so you dont mess up 
			//the card selection
			//return;
		}
		//doing things if a robot has been selected to move/attack
		if(turnState==TurnState.ROBOTSELECT) {
			System.out.println("doing things");
			turnState=TurnState.IDLE;
			//moving the robot if the robot is able to move
			if(selectedBot.canMove()) {
				String seperator=String.valueOf(NetworkData.SEPERATOR);
				dataToSend=NetworkData.MOVE+seperator+selectedBot.getX()
				+seperator+selectedBot.getY()+seperator+mouseLoc.x+seperator+mouseLoc.y;
				if(!selectedBot.move(mouseLoc.x, mouseLoc.y)) {
					dataToSend="";
					
					
										
				}
			//attacking if the robot has already moved and can still attack
			}else if (selectedBot.canAttack()) {
				String seperator=String.valueOf(NetworkData.SEPERATOR);
				dataToSend=NetworkData.ATTACK+seperator+selectedBot.getX()
				+seperator+selectedBot.getY()+seperator+mouseLoc.x+seperator+mouseLoc.y;
				if(!selectedBot.attack(mouseLoc.x, mouseLoc.y)) {
					dataToSend="";
					
					
				}
			}
			selectedBot=null;
			turnState=TurnState.IDLE;	
			
		//if a robot isnt selected and they click on a robot, is should be selected
		}else if(Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y) instanceof Robot) {
			//selecting the robot
			selectedBot=(Robot) Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);	
			turnState = TurnState.ROBOTSELECT;
		}
		//buying a card if they have clicked on the buy a card button
		if(buyCard.isPressed()) {
			buyCard();
		}
		if(endTurn.isPressed()) {
			doneTurn=true;
			//dataToSend=NetworkData.ENDTURN+"";
		}
		selectCard();
	}
	public void buyCard() {
		int cardCost=1;//it costs $1 to buy a card
		int id=deck.get(ThreadLocalRandom.current().nextInt(0, deck.size()));
		//exiting the method if they cant afford it
		if(money<cardCost) {
			return;
		}
		//adding the card to their hand
		hand.add(id);
		//taking away the cost of the card
		money-=cardCost;
		dataToSend=NetworkData.BUYCARD+"";
		
		//this part creates the card out of a few  pictures and textboxes
		Group card = new Group();//the card to be added
		
		
		
		card.setTranslateX(10*FinalProject.PIXEL_SCALE);
		
		//adding the background to the card
		card.getChildren().add(new ImageView(Assets.cardFront));

		//the picture of the robot
		ImageView robotPic = null;//initializing it to null
		//the text that says its price and hp
		Text info = new Text(5*FinalProject.PIXEL_SCALE,32*FinalProject.PIXEL_SCALE,"");
		//setting the font and colour of the text
		info.setFill(Color.WHITE);
		info.setFont(Assets.font);

		//adding the right picture and info depending on the robot that is on the card
		switch(id) {
		case Tank.ID:
			info.setText("$"+Tank.COST+" HP:"+Tank.MAXHEALTH);
			robotPic=new ImageView(Assets.tank);
			robotPic.setTranslateX(Tank.XOFFSET*FinalProject.PIXEL_SCALE);
			robotPic.setTranslateY(Tank.YOFFSET*FinalProject.PIXEL_SCALE);
			break;
		case HeliBot.ID:
			info.setText("$ "+HeliBot.COST+" HP:"+HeliBot.MAXHEALTH);
			robotPic=new ImageView(Assets.heliBot);
			break;
		case TreadBot.ID:
			info.setText("$"+TreadBot.COST+" HP:"+TreadBot.MAXHEALTH);
			robotPic=new ImageView(Assets.treadBot);
			robotPic.setTranslateY(TreadBot.YOFFSET*FinalProject.PIXEL_SCALE);
			break;
		case Turret.ID:
			info.setText("$"+Turret.COST+" HP:"+Turret.MAXHEALTH);
			robotPic=new ImageView(Assets.turret);
			break;
		}
		//putting the picture on the right place in the card
		robotPic.setX(4*FinalProject.PIXEL_SCALE);
		robotPic.setY(4*FinalProject.PIXEL_SCALE);

		//adding the picture and text to the card
		card.getChildren().add(robotPic);
		card.getChildren().add(info);

		//adding the card to the project and the hand
		handPics.add(card);
		project.add(card);

		
	}
	
	private void buyBot(Point loc, int id) {
		turnState=TurnState.IDLE;
		selecttionArrow.setVisible(false);
		handPics.get(selectedCard).setTranslateX(10*FinalProject.PIXEL_SCALE);
		if(loc.x>=0&&loc.y>=0&&loc.x<=1&&loc.y<Board.HEIGHT
				&&Entity.getManager().getEntity(loc.x, loc.y)==null) {
			switch(id) {
			case HeliBot.ID:
				if(money<HeliBot.COST) {
					return;//exiting the method so it doesnt try to place a robot if you dont have enough money
					
				} 
				money-=HeliBot.COST;
				new HeliBot(project, playerNum,loc.x,loc.y).endTurn();
				break;//exiting the switch case so the other parts wont run
				
			case Tank.ID:
				if(money<Tank.COST) {
					return;
				}
				money-=Tank.COST;
				new Tank(project, playerNum,loc.x,loc.y).endTurn();
				break
				;
			case Turret.ID:
				if(money<Turret.COST) {
					return;
				}
				money-=Turret.COST;
				new Turret(project, playerNum,loc.x,loc.y).endTurn();
				break;
				
			case TreadBot.ID:
				if(money<TreadBot.COST) {
					return;
				}
				money-=TreadBot.COST;
				new TreadBot(project, playerNum,loc.x,loc.y).endTurn();
				break;
			}
			String seperator=String.valueOf(NetworkData.SEPERATOR);
			dataToSend=NetworkData.BUYBOT+seperator+id+seperator+loc.x+seperator+loc.y;
			turnState=TurnState.IDLE;
			hand.remove(selectedCard);
			project.remove(handPics.get(selectedCard));
			handPics.remove(selectedCard);
			
			selectedCard=-1;//setting the selected card back to nothing
		}		
	}
	
	private void selectCard() {
		for(int i=hand.size()-1;i>=0;i--) {//i loop backwards so the cards on top will be checked first
			//doing things if the card has been clicked so it can be played
			if(handPics.get(i).isPressed()&&turnState==TurnState.IDLE) {
				turnState=TurnState.BUYBOT;
				selecttionArrow.setVisible(true);
				selecttionArrow.setY((i*35+12+22)*FinalProject.PIXEL_SCALE);
				handPics.get(i).setTranslateX(17*FinalProject.PIXEL_SCALE);
				//making sure we can tell what card they seleected outside of the loop
				selectedCard=i;
			}
		}
	}
	
	private void highlightSelection() {
		//showing that the robot has been selected by changing its tile
		board.selectTile(selectedBot.getX(),selectedBot.getY());
		//showing the spaces it can attack/move
		if(((Robot) selectedBot).canMove()) {//showing movable tiles if it can move by highlighting them
			for(Point i:((Robot) selectedBot).movableTiles()) {
				board.highlightTile(i.x, i.y);
			}
		//if it cant move but can still attack it will show the attackable tiles
		}else if(((Robot) selectedBot).canAttack()) {
			for(Point i:((Robot) selectedBot).attackableTiles()) {
				board.highlightTile(i.x, i.y);
			}
		}
	}
	
	private void showInfo(Point mouseLoc) {
		Entity e=Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);
		endTurn.setVisible(false);
		if(e!=null) {
			infoText.setText(e.getDescription().toUpperCase());
		}else if(buyCard.isHover()) {
			infoText.setText("buy a card for $1");
		}else {
			endTurn.setVisible(true);
			infoText.setText("");
		}
		
	}
	public String getDataToSend() {
		String value=dataToSend;
		dataToSend="";
		
		return value;
	}
}
