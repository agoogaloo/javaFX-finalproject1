package finalProject1.entities.robots;

import java.awt.Point;
import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.board.Board;
import javafx.scene.image.ImageView;

public class BomityBomb extends Robot{
	public static final int ID=5, MAXHEALTH=1, COST=3;
	
	public BomityBomb(FinalProject project,int player) {
		this(project,player, 0, 0);
	}
	public BomityBomb(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.bomb),player,x,y);
		description ="it cant move but it will blow up anything and everything around it"
				+ "\n it can also attack the turn it's placed";
		health=MAXHEALTH;
		range=2;
		damage=2;
		speed=0;
		attacked=false;
	}
	
	@Override
	public void startTurn() {
		moved=true;//the bomb cant move so when it starts its turn it shouldnt be able to move
		attacked=false;
	}
	
	@Override
	public boolean attack(int x, int y) {
		//making it attack even if you dont click on an entity
		if(!attackableTiles().contains(new Point(x,y))||getManager().getEntity(x, y)==this) {
			return false;
		}
		//making everything around it get damaged
		for(Point i: attackableTiles()) {//checking every tile it can attack
			if(getManager().getEntity(i.x, i.y)!=null) {
				getManager().getEntity(i.x, i.y).damage(damage);//damaging it if it exists
			}
		}
		return true;
	}
	
	@Override
	public ArrayList<Point> attackableTiles() {
		//this attacks in a square so the attackable tiles are completely differnt
		ArrayList<Point> arr = new ArrayList<>();//making the arraylist
		//checking in a square with the length of its range
		for(int i=-range;i<=range;i++) {
			for(int j=-range;j<=range;j++) {
				//making sure the tile is actually within the board, and actually a place the robot can reach
				if(Math.abs(i)+Math.abs(j)<=range&&
						x+i>=0&&y+j>=0&&x+i<Board.WIDTH&&y+j<Board.HEIGHT) {
					arr.add(new Point(x+i,y+j)); //adding the point if it is a movable tile
				}
			}
		}
		return arr;//returning the values
	}
}
