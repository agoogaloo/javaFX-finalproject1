package finalProject1.entities;

import java.awt.Point;

import finalProject1.FinalProject;
import finalProject1.board.Board;
import javafx.scene.image.ImageView;

public abstract class Entity {
	private static EntityManager manager=new EntityManager();//this stores all the entities
	private ImageView img;
	protected int x, y;// the entities location in tiles
	private int xOffset=0, yOffset=0;
	protected int health=1;
	protected int player;
	protected boolean alive=true;
	

	
	/**
	 * 
	 * @param project - what the entity will be added to
	 * @param img - an image that is what the entity will look like
	 */
	public Entity(FinalProject project, ImageView img, int player) {
		this.img=img;
		this.player=player;
		if(player==2) {
			System.out.println("eeee");
			img.setScaleX(-1);
		}
		project.add(img);
		manager.addEntity(this);//adding itself to the entitymanager
	}
	
	
	public void update() {
		Point loc= Board.tilesToPixels(x, y);
		img.setX(loc.x+xOffset*FinalProject.PIXEL_SCALE);
		img.setY(loc.y+yOffset*FinalProject.PIXEL_SCALE);
		
		if(health<=0) 
			alive=false;
	}
	
	public void damage(int damage) {
		health-=damage;
		System.out.println(health+"health left");
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
	public ImageView getImg() {
		return img;
	}
	public static EntityManager getManager() {
		return manager;
	}
}
