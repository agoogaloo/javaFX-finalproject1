package finalProject1.states.gameState;

import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
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

/**
 * this is the other player that you play against while playing the game
 */
public class Opponent extends GamePlayer{
	private NetworkData data;//the network data that they use to communicate with the other computer
	
	//the other computer needs to start with 1 card because when they draw 2 at the beginning of the match
	//they only send the data once
	private int cards=1;
	private ArrayList<ImageView> handPics = new ArrayList<>();//the pictures of the cards
	private Group hand=new Group();//the group all the cards will be added to
	
	
	
	public Opponent(NetworkData data,FinalProject project) {
		super(project);
		playerNum=2;
		this.data=data;
		moneyText.setX(245*FinalProject.PIXEL_SCALE);
	
		money+=1;
		//adding the hand to the project so things can be seen
		project.add(hand);
	}

	@Override
	public void update() {
		//updating the money text so we can see how much money the opponent has
		moneyText.setText("$"+money);
		//updating the hand graphic if it doesnt match how many cards are really in their hand
		if(handPics.size()!=cards) {
			//reseting the arraylist and group so we can redo everything
			handPics.clear();
			hand.getChildren().clear();
			//adding a card image for every card in their hand
			for(int i=0;i<cards;i++) {
				handPics.add(new ImageView(Assets.cardBack));
				handPics.get(i).setX(402*FinalProject.PIXEL_SCALE);
				handPics.get(i).setY((i*35+12)*FinalProject.PIXEL_SCALE);
				hand.getChildren().add(handPics.get(i));
				
			}
		}
		
		//getting the data that was sent by the opponent
		String[] moves=data.getData().split(NetworkData.SEPERATOR+"");
		
		//exiting if the opponent hasnt sent anything
		if(moves[0].length()==0) {
			return;
		}
		
		//printing what sent
		for(String i:moves) {
			System.out.print(i+", ");
		}
		System.out.println("");
		
		
		//doing things depending on what move the other computer sent
		switch (moves[0].charAt(0)) {
		case NetworkData.STARTTURN:
			doneTurn=false;//starting the turn
			break;	
			
		case NetworkData.ENDTURN:
			doneTurn=true;//ending the turn
			break;	
			
		case NetworkData.BUYCARD:
			cards++;//buying a card
			money-=1;
			break;
			
		case NetworkData.BUYBOT:
			cards--;//buying a robot
			//getting the location and type of robot
			int x=Integer.parseInt(moves[2]),y=Integer.parseInt(moves[3]);
			x=x*-1+6;//the opponents board is mirrored so the x coordinate should be flipped
			System.out.println("placed at "+x+", "+y);
			//placing the right robot in the right place and subtracting the right amount of money
			switch(Integer.parseInt(moves[1])) {
			case HeliBot.ID:
				
				money-=HeliBot.COST;
				new HeliBot(project, playerNum,x,y).endTurn();
				break;//exiting the switch case so the other parts wont run
		
				
			case BomityBomb.ID:
				if(money<BomityBomb.COST) {
					return;//exiting the method so it doesnt try to place a robot if you dont have enough money
					
				} 
				money-=BomityBomb.COST;
				new BomityBomb(project, playerNum,x,y);
				break;//exiting the switch case so the other parts wont run
			
			case Tank.ID:
				if(money>=Tank.COST) {
					money-=Tank.COST;
					new Tank(project, playerNum,x,y).endTurn();
					break;
				}
				return;
			case Turret.ID:
				if(money>=Turret.COST) {
					money-=Turret.COST;
					new Turret(project, playerNum,x,y).endTurn();
					break;
				}
				return;
				
			case TreadBot.ID:
				if(money>=TreadBot.COST) {
					money-=TreadBot.COST;
					new TreadBot(project, playerNum,x,y).endTurn();
					break;
				}
				return;
			}
			break;
			
		case NetworkData.ATTACK:
			//attacking
			int[]attack=new int[4];
			//getting who attacked and where
			for(int i=0;i<4;i++) {
				attack[i] = Integer.parseInt(moves[i+1]);
			}
			//attacking using the data
			attackBot(attack);
			break;
			
		case NetworkData.MOVE:
			//moving robots
			System.out.println("enemy is moving");
			int[]move=new int[4];
			//getting the data from the string
			for(int i=0;i<4;i++) {
				move[i] = Integer.parseInt(moves[i+1]);
			}
			//moving the robot
			moveBot(move);
			break;
		}
	}
	
	/**
	 * this lets us move a robot from an array of ints that we get sent by the opponent
	 * @param moveData - an array that holds which robot moves and where
	 */
	private void moveBot(int[] moveData) {
		//moving opponents robots
		if(Entity.getManager().getEntity(moveData[0]*-1+6, moveData[1]) instanceof Robot) {
			//flipping the x coordinate because the boards are flipped
			((Robot) Entity.getManager().getEntity(moveData[0]*-1+6, moveData[1])).move(moveData[2]*-1+6,moveData[3]);
		}else {
			System.out.println("no robot at "+(moveData[0]*-1+6)+ ", "+moveData[1]);
		}
		
	}
	/**
	 * like the move robot method but it attacks instead
	 * @param attackData
	 */
	private void attackBot(int[] attackData) {
		//opponent is attacking
		if(Entity.getManager().getEntity(attackData[0]*-1+6, attackData[1]) instanceof Robot) {
			
			((Robot) Entity.getManager().getEntity(attackData[0]*-1+6, attackData[1]))
			.attack(attackData[2]*-1+6,attackData[3]);
		}else {
			System.out.println("no robot at "+attackData[0]*-1+6+ attackData[1]);
		}
		
	}
	
	
}
