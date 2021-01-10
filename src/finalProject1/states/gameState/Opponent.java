package finalProject1.states.gameState;

import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
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
import javafx.scene.text.Text;

public class Opponent extends GamePlayer{
	private NetworkData data;
	
	private int cards=1;
	private ArrayList<ImageView> handPics = new ArrayList<>();//the pictures of the cards
	private Group hand=new Group();
	
	
	
	public Opponent(NetworkData data,FinalProject project) {
		super(project);
		playerNum=2;
		this.data=data;
		moneyText.setX(245*FinalProject.PIXEL_SCALE);
	
		money+=1;
		project.add(hand);
	}

	@Override
	public void startTurn() {
		super.startTurn();
		data.sendData(NetworkData.STARTTURN+"");
	}
	@Override
	public void update() {
		moneyText.setText("$"+money);
		if(handPics.size()!=cards) {
			System.out.println("changing cards in opponents hand they should have "+cards);
			handPics.clear();
			hand.getChildren().clear();
			for(int i=0;i<cards;i++) {
				handPics.add(new ImageView(Assets.cardBack));
				handPics.get(i).setX(402*FinalProject.PIXEL_SCALE);
				handPics.get(i).setY((i*35+12)*FinalProject.PIXEL_SCALE);
				hand.getChildren().add(handPics.get(i));
				
			}
		}
		
		//getting the data that was sent by the opponent
		String[] moves=data.getData().split(NetworkData.SEPERATOR+"");
		
		if(moves[0].length()==0) {
			return;
		}
		
		for(String i:moves) {
			System.out.print(i+", ");
		}
		System.out.println("");
		
		
		switch (moves[0].charAt(0)) {
		case NetworkData.STARTTURN:
			doneTurn=false;
			break;	
			
		case NetworkData.ENDTURN:
			doneTurn=true;
			break;	
			
		case NetworkData.BUYCARD:
			cards++;
			money-=1;
			break;
			
		case NetworkData.BUYBOT:
			cards--;
			int x=Integer.parseInt(moves[2]),y=Integer.parseInt(moves[3]);
			x=x*-1+6;//the opponents board is mirrored so the x coordinate should be flipped
			switch(Integer.parseInt(moves[1])) {
			case HeliBot.ID:
				
				money-=HeliBot.COST;
				new HeliBot(project, playerNum,x,y).endTurn();
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
			}
			break;
			
		case NetworkData.ATTACK:
			int[]attack=new int[4];
			for(int i=0;i<=4;i++) {
				try {
					attack[i] = Integer.parseInt(moves[i+1]);
				} catch (NumberFormatException e) {
					System.out.println("the opponent's data didnt send a number after their attack"
							+ "something has gone wrong");
					return;
				}
			}
			attackBot(attack);
			break;
			
		case NetworkData.MOVE:
			int[]move=new int[4];
			for(int i=0;i<=4;i++) {
				try {
					move[i] = Integer.parseInt(moves[i+1]);
				} catch (NumberFormatException e) {
					System.out.println("the opponent's data didnt send a number after their move"
							+ "something has gone wrong");
					return;
				}
			}
			moveBot(move);
			break;
		}
	}
	
	private void moveBot(int[] moveData) {
		if(Entity.getManager().getEntity(moveData[0], moveData[1]) instanceof Robot) {
			((Robot) Entity.getManager().getEntity(moveData[0], moveData[1])).move(moveData[2],moveData[3]);
		}
		
	}
	private void attackBot(int[] attackData) {
		if(Entity.getManager().getEntity(attackData[0], attackData[1]) instanceof Robot) {
			((Robot) Entity.getManager().getEntity(attackData[0], attackData[1])).attack(attackData[2],attackData[3]);
		}
		
	}
	
	
}
