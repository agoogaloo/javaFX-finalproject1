package finalProject1.entities.robots;

import finalProject1.FinalProject;
import finalProject1.board.Board;
import finalProject1.entities.Entity;
import javafx.scene.image.ImageView;

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
		if(x<0||y<0||x>=Board.WIDTH||y>=Board.HEIGHT||Entity.getManager().getEntity(x, y)!=null)
			return false; 
		//making not letting the robot move outside of it's range
		if(Math.abs(this.x-x)+Math.abs(this.y-y)>speed)
			return false ; 
		
		this.x=x;
		this.y=y;
		moved=true;
		return true;
	}
	
	public boolean attack(int x, int y) {
		if(getManager().getEntity(x, y)!=null&&(x==this.x||y==this.y)&&(Math.abs(this.x-x)+Math.abs(this.y-y)<=range)) {
			getManager().getEntity(x, y).damage(damage);
			attacked=true;
			return true;
		}
		return false;
	}
	
	public boolean canMove() {
		return !moved;
	}
	public boolean canAttack() {
		return !attacked;
	}

}
