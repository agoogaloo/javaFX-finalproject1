package finalProject1.entities;

import java.awt.Point;

import finalProject1.FinalProject;
import finalProject1.board.Board;
import javafx.scene.image.ImageView;

/**
 *this class is for any object that is on the board and can be interacted with
 */
public abstract class Entity {
	
	private static EntityManager manager=new EntityManager();//this stores all the entities
	private ImageView img;//what the entity looks like
	protected int x, y;// the entities location in tiles
	private int xOffset=0, yOffset=0;//
	protected int health=1;//how mush health the robot has left
	protected int player;//who controls this robot
	protected boolean alive=true;//if the robot if alive of not
	protected String infoText = "";//this holds all the text and stuff that lets us know things about the entity
	protected String description = "";
	

	/**
	 * 
	 * @param project - what the entity will be added to
	 * @param img - an image that is what the entity will look like
	 * @param player - what team the entity is on
	 */
	public Entity(FinalProject project, ImageView img, int player) {
		this.img=img;
		this.player=player;
		//flipping the image if it is on player 2s team
		if(player==2) {
			img.setScaleX(-1);
		}
		
		//adding the image so its visible
		project.add(img);
		
		manager.addEntity(this);//adding itself to the entity manager
	}
	
	/**
	 * this is called every frame
	 */
	public void update() {
		Point loc= Board.tilesToPixels(x, y);//the robots location in pixels
		//offsetting the picture if it shoud be offset to look better
		img.setX(loc.x+xOffset*FinalProject.PIXEL_SCALE);
		img.setY(loc.y+yOffset*FinalProject.PIXEL_SCALE);
	}
	/**
	 * this lets other entities damage this entity
	 * @param damage - how much damage it takes
	 */
	public void damage(int damage) {
		health-=damage;//damaging the entity
		//making it dead and removing it it it has no health left
		if(health<=0) {
			alive=false;
			img.setImage(null);
		}
	}
	
	//getters/setters
	protected void setxOffset(int xOffset) {
		this.xOffset = xOffset;
		if(player==2) this.xOffset*=-1;
	}
	protected void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}
	public boolean isAlive() {
		return alive;
	}
	
	public int getPlayer() {
		return player;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getDescription() {
		return "HP:"+health+"\n"+description;
	}	
	
	public ImageView getImg() {
		return img;
	}
	public static EntityManager getManager() {
		return manager;
	}


}
