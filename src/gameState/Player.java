package gameState;

import java.awt.Point;
import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.Inputs;
import finalProject1.board.Board;
import finalProject1.entities.robots.HeliBot;
import finalProject1.entities.robots.Tank;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Player {
	public final int PLAYERNUM;
	FinalProject project;
	
	private int money;
	private Text moneyText;
	
	private ArrayList<Integer> hand = new ArrayList<>();
	private ArrayList<Group> handPics = new ArrayList<>();
	
	private int selectedCard=-1;
	

	public Player(int playerNum, FinalProject project) {
		PLAYERNUM=playerNum;
		this.project=project;

		//initializing the textbox that shows how much money they have
		if(playerNum==1) {
			moneyText = new Text(195*FinalProject.PIXEL_SCALE,30," uh oh, this should have changed to a number");
		}else if(playerNum==2) {
			moneyText = new Text(245*FinalProject.PIXEL_SCALE,30," b");
		}
		moneyText.setFill(Color.WHITESMOKE);
		moneyText.setFont(Assets.font);

		project.add(moneyText);
		money = 7;
		buyCard(Tank.ID);
		buyCard(HeliBot.ID);
	}

	public void update() {
		moneyText.setText("$"+money);
		
		for(int i=hand.size()-1;i>=0;i--) {
			//setting the card to be at the right height in case other cards have been played
			handPics.get(i).setTranslateY((i*35)*FinalProject.PIXEL_SCALE);
			if(handPics.get(i).isPressed()) {
				if(hand.get(i)==HeliBot.ID&&money>=HeliBot.COST) {
					money-=HeliBot.COST;
				}else if(hand.get(i)==Tank.ID&&money>=Tank.COST) {
					money-=Tank.COST;
				}else {
					break;
				}
				handPics.get(i).toFront();
				selectedCard=i;
			}
		}

	}

	public void placeBot() {
		hand.remove(selectedCard);
		project.remove(handPics.get(selectedCard));
		handPics.remove(selectedCard);
		selectedCard=-1;
	}

	public void addMoney(int amount) {
		money+=amount;
	}

	public void buyCard(int id) {
		int cardCost=1;
		if(money<cardCost) {
			return;
		}
		hand.add(id);
		Group card = new Group();
		
		//putting the card on the left or right depending on what player is drawing the card
		if(PLAYERNUM==1) {
			card.setTranslateX(10*FinalProject.PIXEL_SCALE);
		}else if(PLAYERNUM==2) {
			card.setTranslateX(402*FinalProject.PIXEL_SCALE);
		}

		ImageView cardPic = new ImageView(Assets.cardFront);

		card.getChildren().add(cardPic);

		ImageView robotPic = null;
		Text info = new Text(5*FinalProject.PIXEL_SCALE,32*FinalProject.PIXEL_SCALE,"");
		info.setFill(Color.WHITE);
		info.setFont(Assets.font);

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
		}
		robotPic.setX(4*FinalProject.PIXEL_SCALE);
		robotPic.setY(4*FinalProject.PIXEL_SCALE);

		card.getChildren().add(robotPic);
		card.getChildren().add(info);

		handPics.add(card);
		project.add(card);

		money-=cardCost;
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
