package finalProject1.entities.robots;

import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.board.Board;
import finalProject1.entities.Entity;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public abstract class Robot extends Entity{
	
	protected int range, speed, damage;
	private boolean moved, attacked;
	
	
	
	
	public Robot(FinalProject project,ImageView img,int player ) {
		this(project, img,player, 0, 0);
	}
	public Robot(FinalProject project,ImageView img, int player ,int x, int y) {
		super(project, img,player);
		this.x=x;
		this.y=y;
		Text infoText=new Text(150*FinalProject.PIXEL_SCALE,220*FinalProject.PIXEL_SCALE,"information stuff, woo cool info");
		infoText.setFont(Assets.font);
		info.getChildren().add(infoText);
	}
	
	public void startTurn() {
		moved=false;
		attacked=false;
	}

	public void endTurn() {
		moved=true;
		attacked=true;
	}
	
	public boolean move(int x, int y) {
		//not letting you move the robot off the board or ontop of another robot
		if(!movableTiles().contains(new Point(x,y))) {
			return false;
		}
		this.x=x;
		this.y=y;
		moved=true;
		return true;
	}
	
	public boolean attack(int x, int y) {
		if(!attackableTiles().contains(new Point(x,y))&&getManager().getEntity(x, y)!=null) {
			return false;
		}
		getManager().getEntity(x, y).damage(damage);
		attacked=true;
		return true;
	}
	
	
	//getters/setters
	public ArrayList<Point> attackableTiles(){
		ArrayList<Point> arr = new ArrayList<>();
		for(int i=0;i<=range;i++) {
			arr.add(new Point(x,y+i));
			arr.add(new Point(x,y-i));
			arr.add(new Point(x+i,y));
			arr.add(new Point(x-i,y));
		}
		return arr;
		
	}
	
	public ArrayList<Point> movableTiles(){
		ArrayList<Point> arr = new ArrayList<>();
		for(int i=-speed;i<=speed;i++) {
			for(int j=-speed;j<=speed;j++) {
				if(Math.abs(i)+Math.abs(j)<=speed&&getManager().getEntity(x+i,y+j)==null) {
					arr.add(new Point(x+i,y+j)); 
				}
			}
		}
		return arr;
		
	}
	
	
	public boolean canMove() {
		return !moved;
	}
	public boolean canAttack() {
		return !attacked;
	}

}
