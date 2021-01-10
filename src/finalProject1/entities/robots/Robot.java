package finalProject1.entities.robots;

import java.awt.Point;
import java.util.ArrayList;

import finalProject1.FinalProject;
import finalProject1.board.Board;
import finalProject1.entities.Entity;
import javafx.scene.image.ImageView;

public abstract class Robot extends Entity{
	
	protected int range, speed, damage;//its attack stats
	//whether it has moved or attacked this turn
	protected boolean moved, attacked;
	
	/**
	 * a constructor that makes a new robot at 0,0
	 * @param project
	 * @param img
	 * @param player
	 */
	public Robot(FinalProject project,ImageView img,int player) {
		this(project, img,player, 0, 0);
	}
	/**
	 * a constructor for the robot that lets you spec=cify where it should start
	 * @param project - the project it is being added to
	 * @param img - the picture of the robot
	 * @param player - who owns the robot
	 * @param x - its x coordinate in tiles
	 * @param y - its y coordinate in tiles
	 */
	public Robot(FinalProject project,ImageView img, int player ,int x, int y) {
		super(project, img,player);
		this.x=x;
		this.y=y;
	}
	/**
	 * this is called at the start of the robots turn
	 */
	public void startTurn() {
		//letting it move and attack again
		moved=false;
		attacked=false;
	}

	/**
	 * this is called once the turn is over
	 */
	public void endTurn() {
		//making them unable to move or attack if it isnt their turn
		moved=true;
		attacked=true;
	}
	
	
	
	/**
	 * this lets robots move to a selected tile
	 * @param x - the x coodrdinate in tiles
	 * @param y - the y coordinate in tiles
	 * @return - whether the robot was able to move there or not
	 */
	public boolean move(int x, int y) {
		//checking if it is able to move there
		if(!movableTiles().contains(new Point(x,y))) {
			return false;//exiting the function and saying it wasnt able to move
		}
		//moving
		this.x=x;
		this.y=y;
		//saying that it has already moved and cant move again this turn
		moved=true;
		return true;
	}
	/**
	 * makes the robot attack the selected tile
	 * @param x - x coordinate in tiles
	 * @param y - y coordinate in tiles
	 * @return - whether it was able to attack or not
	 */
	public boolean attack(int x, int y) {
		//exiting the finction if it tries to attack itself, nothing, or a tile that it cant reach
		if(!attackableTiles().contains(new Point(x,y))||getManager().getEntity(x, y)==null||getManager().getEntity(x, y)==this) {
			return false;
		}
		
		System.out.println(getManager().getEntity(x, y));//getting the entity at the spot it is attacking
		getManager().getEntity(x, y).damage(damage);//damaging it
		attacked=true;//saying it has attacked and cant attack again this turn
		return true;
	}
	
	/**
	 * this gets the places this robot is able to attack
	 * @return - an array of points which hold the coordinates of any attackable tiles 
	 */
	public ArrayList<Point> attackableTiles(){
		ArrayList<Point> arr = new ArrayList<>();//making the arraylist
		for(int i=0;i<=range;i++) {//looping through its range
			//adding the tiles in for directions so it will be able to shoot in straight lines 
			arr.add(new Point(x,y+i));
			arr.add(new Point(x,y-i));
			arr.add(new Point(x+i,y));
			arr.add(new Point(x-i,y));
		}
		return arr;//returning the valuse
		
	}
	
	/**
	 * this gets the places this robot is able to move
	 * @return - an array of points which hold the coordinates of any movable tiles 
	 */
	public ArrayList<Point> movableTiles(){
		ArrayList<Point> arr = new ArrayList<>();//making the arraylist
		//checking in a square with the length of its range
		for(int i=-speed;i<=speed;i++) {
			for(int j=-speed;j<=speed;j++) {
				//making sure the tile is empty, is actually within the, and actually a place the robot can reach
				if(Math.abs(i)+Math.abs(j)<=speed&&getManager().getEntity(x+i,y+j)==null&&
						x+i>=0&&y+j>=0&&x+i<Board.WIDTH&&y+j<Board.HEIGHT) {
					arr.add(new Point(x+i,y+j)); //adding the point if it is a movable tile
				}
			}
		}
		return arr;//returning the values
		
	}
	
	//getters/setters
	/**
	 * returns a string with all the robots stats and stuff
	 */
	@Override
	public String getDescription() {
		return "HP: "+ health+"    SPD: "+speed+"    DMG: "+damage+"    RNG: "+range+"\n"+description.toUpperCase();
	}
	public boolean canMove() {
		return !moved;
	}
	
	public boolean canAttack() {
		return !attacked;
	}
	public int getDamage() {
		return damage;
	}
	public int getRange() {
		return range;
	}
	public int getSpeed() {
		return speed;
	}
}
