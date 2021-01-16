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
import finalProject1.entities.robots.BomityBomb;
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

/**
 * this is the player you play as when you play the game
 *
 */
public class Player extends GamePlayer{

	private TurnState turnState=TurnState.IDLE;//what 
	private Board board;//the board the game is played on
	private Robot selectedBot=null;
	private int selectedCard=-1;
	//the data that is to be sent to the opponent that tells them what youre doing
	private String dataToSend="";
	
	
	private Text turnText = new Text(120*FinalProject.PIXEL_SCALE,150*FinalProject.PIXEL_SCALE,"WAITING FOR YOUR OPPONENT");
	private Text infoText = new Text(113*FinalProject.PIXEL_SCALE,230*FinalProject.PIXEL_SCALE,"");
	
	private ArrayList<Integer> deck = new ArrayList<>(Arrays.asList(Tank.ID,HeliBot.ID, TreadBot.ID, 
			Turret.ID,BomityBomb.ID));
	
	private ArrayList<Integer> hand = new ArrayList<>();//the id of the robots in their hand
	private ArrayList<Group> handPics = new ArrayList<>();//the pictures of the cards
	private ImageView buyCard = new ImageView(Assets.deck);//the button to buy a new card
	//the arrow that appears showing you what card your selecting
	private ImageView selecttionArrow = new ImageView(Assets.arrow);
	
	private Group endTurn=new Group();//the button you click to end your turn
	
	public Player(Board board,FinalProject project) {
		super(project);
		this.board=board;
		playerNum=1;
		//setting the location of the deck to buy from
		buyCard.setX(208*FinalProject.PIXEL_SCALE);
		buyCard.setY(3*FinalProject.PIXEL_SCALE);
		
		//the arrow will always have the same x so i set it here
		selecttionArrow.setX(10*FinalProject.PIXEL_SCALE);
		selecttionArrow.setVisible(false);//making it invisible because nothing is selected
		
		
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
		
		//doing text things
		turnText.setFont(Assets.boldfont);
		turnText.setFill(Color.WHITE);
		turnText.toFront();
		
		
		infoText.setFont(Assets.font);
		infoText.setFill(Color.WHITE);
		
		//adding everything
		project.add(turnText);
		project.add(infoText);
		project.add(endTurn);
		project.add(buyCard);
		project.add(selecttionArrow);
		
		//making you start with 2 cards
		money+=2;
		buyCard();
		buyCard();		
	}
	
	@Override
	public void update() {
		Point mouseLoc=Board.PixelsToTiles(Inputs.getX(), Inputs.getY());
		//saying whether you are still waiting for your opponent to finish or not
		if(doneTurn) {
			turnText.setVisible(true);
		}else {
			turnText.setVisible(false);
		}
		
		showInfo(mouseLoc);//showing info on the tile being hoverd over
		moneyText.setText("$"+money);//updating the money display
		
		//only letting you click things if it is still your turn
		if(Inputs.isClicked()&&!doneTurn) {
			onClick(mouseLoc);
		}
		
		//correcting the vertical position of the cards
		for(int i=hand.size()-1;i>=0;i--) {//i loop backwards so the cards on top will be checked first		
			//setting the card to be in the right place case other cards have been played
			handPics.get(i).setTranslateY((i*35+12)*FinalProject.PIXEL_SCALE);
		}
		//letting you know what card you have selected
		if(turnState==TurnState.ROBOTSELECT) {
			highlightSelection();
		}
		
	}
	
	private void onClick(Point mouseLoc) {
		if(turnState==TurnState.BUYBOT) {
			//if a card is already selected then it will buy it
			buyBot(mouseLoc,hand.get(selectedCard));
		}
		//doing things if a robot has been selected to move/attack
		if(turnState==TurnState.ROBOTSELECT) {
			//the robot should be unselected after you click again
			turnState=TurnState.IDLE;
			//moving the robot if the robot is able to move
			if(selectedBot.canMove()) {
				//telling the opponent that we moveed a robot and they should as well
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
		case BomityBomb.ID:
			info.setText("$ "+BomityBomb.COST+" HP:"+BomityBomb.MAXHEALTH);
			robotPic=new ImageView(Assets.bomb);
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
	
	/**
	 * this lets the player buy and place the robot they select
	 * @param loc - where the robot is being placed
	 * @param id - the id of the robot being bought
	 */
	private void buyBot(Point loc, int id) {
		turnState=TurnState.IDLE;//going back to an idle state
		selecttionArrow.setVisible(false);//making the selection arrow go away
		//moving the card back to its original place
		handPics.get(selectedCard).setTranslateX(10*FinalProject.PIXEL_SCALE);
		//making sure there isnt already somthing there and they are trying to place the robot in the 1st 2 rows
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
			case BomityBomb.ID:
				if(money<BomityBomb.COST) {
					return;//exiting the method so it doesnt try to place a robot if you dont have enough money
					
				} 
				money-=BomityBomb.COST;
				new BomityBomb(project, playerNum,loc.x,loc.y).startTurn();
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
			//it returns from the method if it wasnt able to place it so this is only called once the robot is placed
			//telling the opponent to add a robot as well
			String seperator=String.valueOf(NetworkData.SEPERATOR);
			dataToSend=NetworkData.BUYBOT+seperator+id+seperator+loc.x+seperator+loc.y;
			//removing the card from the hand and screen
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
				selecttionArrow.setVisible(true);//making the arrow visible now that a card is selected
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
	
	/**
	 * this shows info for things like entities or the deck of cards
	 * @param mouseLoc - the mouses location in tiles
	 */
	private void showInfo(Point mouseLoc) {
		//getting the entity hovered over or null if there isnt one
		Entity e=Entity.getManager().getEntity(mouseLoc.x, mouseLoc.y);
		endTurn.setVisible(false);//making the end turn button invisible
		if(e!=null) {
			//giving info on entities
			infoText.setText(e.getDescription().toUpperCase());
		}else if(buyCard.isHover()) {
			//giving info on the deck
			infoText.setText("buy a card for $1");
		}else {
			//making the end turn button visible if you arent hovering over something
			endTurn.setVisible(true);
			infoText.setText("");//resenting the info text
		}
		
	}
	/**
	 * this gets any data that needs to be sent to the opponent 
	 * @return - the string of data to send
	 */
	public String getDataToSend() {
		String value=dataToSend;
		dataToSend="";//reseting data to send so it only gets sent once
		//returning the value
		return value;
	}
}
