package finalProject1.entities.robots;

import java.awt.Point;
import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.board.Board;
import javafx.scene.image.ImageView;

public class BomityBomb extends Robot{
	public static final int ID=5, MAXHEALTH=1, COST=2;
	
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
		moved=true;//the turret cant move so when it starts its turn it shouldnt be able to move
		attacked=false;
	}
	@Override
	public boolean attack(int x, int y) {
		if(!attackableTiles().contains(new Point(x,y))||getManager().getEntity(x, y)==this) {
			return false;
		}
		for(Point i: attackableTiles()) {
			if(getManager().getEntity(i.x, i.y)!=null) {
				getManager().getEntity(i.x, i.y).damage(damage);//damaging it
			}
		}
		return true;
	}
	@Override
	public ArrayList<Point> attackableTiles() {
		ArrayList<Point> arr = new ArrayList<>();//making the arraylist
		//checking in a square with the length of its range
		for(int i=-range;i<=range;i++) {
			for(int j=-range;j<=range;j++) {
				//making sure the tile is empty, is actually within the, and actually a place the robot can reach
				if(Math.abs(i)+Math.abs(j)<=range&&
						x+i>=0&&y+j>=0&&x+i<Board.WIDTH&&y+j<Board.HEIGHT) {
					arr.add(new Point(x+i,y+j)); //adding the point if it is a movable tile
				}
			}
		}
		return arr;//returning the values
	}
}
