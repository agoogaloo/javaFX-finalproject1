package finalProject1.gameState;

import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.TurnState;
import finalProject1.entities.robots.HeliBot;
import finalProject1.entities.robots.Tank;
import finalProject1.entities.robots.TreadBot;
import finalProject1.entities.robots.Turret;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Player {
	public final int PLAYERNUM;//which player they are (1 or 2)
	FinalProject project;//the project (needed so it can add graphics to it)
	
	private int money;//how much money they have
	private Text moneyText;//showing how much money they have
	
	//these are parallel arrays for the hand and the graphics that show them
	private ArrayList<Integer> hand = new ArrayList<>();//the id of the robots in their hand
	private ArrayList<Group> handPics = new ArrayList<>();//the pictures of the cards
	
	//this is what card in their hand they are selecting or -1 for none selected
	private int selectedCard=-1;

	
	public Player(int playerNum, FinalProject project) {
		PLAYERNUM=playerNum;
		this.project=project;

		//initializing the textbox that shows how much money they have in the right place
		if(playerNum==1) {
			moneyText = new Text(195*FinalProject.PIXEL_SCALE,30," uh oh, this should have changed to a number");
		}else if(playerNum==2) {
			moneyText = new Text(245*FinalProject.PIXEL_SCALE,30," b");
		}
		//making it the right font and colour
		moneyText.setFill(Color.WHITESMOKE);
		moneyText.setFont(Assets.font);

		//adding the text so we can see it
		project.add(moneyText);
		money = 5;//giving them the starting amount of money
		//giving them 2 starting cards
		buyCard(Tank.ID);
		buyCard(TreadBot.ID);
		
	}

	/**
	 * this runs every frame and updates anything that should be updated
	 * @param turnState - the state of the current turn
	 */
	public void update(TurnState turnState) {
		//setting the text to show the right amount of money
		moneyText.setText("$"+money);
		
		//checking every card to see if it has been clicked
		for(int i=hand.size()-1;i>=0;i--) {//i loop backwards so the cards on top will be checked first
			
			//setting the card to be in the right place case other cards have been played
			handPics.get(i).setTranslateY((i*35+12)*FinalProject.PIXEL_SCALE);
			//doing things if the card has been clicked so it can be played
			if(handPics.get(i).isPressed()&&turnState==TurnState.IDLE) {
				//making sure they have enough money to buy the robot and subtracting it
				switch(hand.get(i)) {
				case HeliBot.ID:
					if(money>=HeliBot.COST) {
						money-=HeliBot.COST;
						break;//exiting the switch case so the other parts wont run
					} 
					return;//exiting the method so it doesnt try to place a robot if you dont have enough money
				case Tank.ID:
					if(money>=Tank.COST) {
						money-=Tank.COST;
						break;
					}
					return;
				case Turret.ID:
					if(money>=Turret.COST) {
						money-=Turret.COST;
						break;
					}
					return;
					
				case TreadBot.ID:
					if(money>=TreadBot.COST) {
						money-=TreadBot.COST;
						break;
					}
					return;
				}
				//adding a highlight to the selected card and bringing it to the front
				Rectangle selection = new Rectangle(40*FinalProject.PIXEL_SCALE,52*FinalProject.PIXEL_SCALE);
				selection.setFill(Color.WHITE);
				selection.setX(-1*FinalProject.PIXEL_SCALE);
				selection.setY(-1*FinalProject.PIXEL_SCALE);
				handPics.get(i).getChildren().add(selection);
				selection.toBack();
				handPics.get(i).toFront();
				//making sure we can tell what card they seleected outside of the loop
				selectedCard=i;
			}
		}
	}

	/**
	 * this is called when the card being selected has been played and should be removed
	 */
	public void placeBot() {
		//removing the card from everything its in
		hand.remove(selectedCard);
		project.remove(handPics.get(selectedCard));
		handPics.remove(selectedCard);
		selectedCard=-1;//setting the selected card back to nothing
	}

	
	/**
	 * this is called when the deck has been clicked and a new card has been bought
	 * @param id - the id of the card being bought
	 */
	public void buyCard(int id) {
		int cardCost=1;//it costs $1 to buy a card
		//exiting the method if they cant afford it
		if(money<cardCost) {
			return;
		}
		//adding the card to their hand
		hand.add(id);
		
		//this part creates the card out of a few  pictures and textboxes
		Group card = new Group();//the card to be added
		
		//putting the card on the left or right depending on what player is drawing the card
		if(PLAYERNUM==1) {
			card.setTranslateX(10*FinalProject.PIXEL_SCALE);
		}else if(PLAYERNUM==2) {
			card.setTranslateX(402*FinalProject.PIXEL_SCALE);
		}
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

		//taking away the cost of the card
		money-=cardCost;
	}
	
	//getters/setters
	public void addMoney(int amount) {
		money+=amount;
	}
	
	public int getPlayerNum() {
		return PLAYERNUM;
	}
	
	public int getMoney() {
		return money;
	}
	
	public int getSelectedBot() {
		
		if(selectedCard==-1) {
			return -1;
		}
		return hand.get(selectedCard);
	}
}
