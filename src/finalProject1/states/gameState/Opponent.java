package finalProject1.states.gameState;

import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.entities.Entity;
import finalProject1.entities.robots.Robot;
import finalProject1.network.NetworkData;
import javafx.scene.image.ImageView;

public class Opponent extends GamePlayer{
	private NetworkData data;
	
	private int cards=2;
	private ArrayList<ImageView> handPics = new ArrayList<>();//the pictures of the cards
	
	
	
	public Opponent(NetworkData data,FinalProject project) {
		super(project);
		playerNum=2;
		this.data=data;
	}

	@Override
	public void startTurn() {
		super.startTurn();
		data.sendData(NetworkData.STARTTURN+"");
	}
	@Override
	public void update() {
		if(handPics.size()!=cards) {
			handPics.clear();
			for(int i=0;i<cards;i++) {
				handPics.add(new ImageView(Assets.cardBack));
				handPics.get(i).setX(402*FinalProject.PIXEL_SCALE);
				handPics.get(i).setY((i*35+12)*FinalProject.PIXEL_SCALE);
				
			}
		}
		
		//getting the data that was sent by the opponent
		String[] moves=data.getData().split(NetworkData.SEPERATOR+"");
		
		if(moves[0].length()==0) {
			return;
		}
		System.out.println("");
		for(String i:moves) {
			System.out.print(i+", ");
		}
		
		
		switch (moves[0].charAt(0)) {
		case NetworkData.STARTTURN:
			doneTurn=false;
			break;	
			
		case NetworkData.ENDTURN:
			doneTurn=true;
			break;	
			
		case NetworkData.CARDS:
			cards= Integer.parseInt(moves[1]);
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
