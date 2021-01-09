package finalProject1.states.gameState;

import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.network.NetworkData;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class Opponent extends GamePlayer{
	private NetworkData data;
	
	private int cards=2;
	private ArrayList<ImageView> handPics = new ArrayList<>();//the pictures of the cards
	
	public Opponent(NetworkData data) {
		this.data=data;
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
		System.out.println(moves.toString());
		if(moves.length==0) {
			return;
		}
		
		switch (moves[0].charAt(0)) {
		case NetworkData.ENDTURN:
			doneTurn=true;
			break;
			
		case NetworkData.CARDS:
			cards= Integer.parseInt(moves[1]);
			break;
			
		case NetworkData.ATTACK:
			for(int i=0;i<=4;i++) {
				try {
					attack[i] = Integer.parseInt(moves[i+1]);
				} catch (NumberFormatException e) {
					System.out.println("the opponent's data didnt send a number after their attack"
							+ "something has gone wrong");
					return;
				}
			}
			break;
			
		case NetworkData.MOVE:
			for(int i=0;i<=4;i++) {
				try {
					move[i] = Integer.parseInt(moves[i+1]);
				} catch (NumberFormatException e) {
					System.out.println("the opponent's data didnt send a number after their move"
							+ "something has gone wrong");
					return;
				}
			}
			break;
		}
	}
	
	
}
