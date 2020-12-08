package entities;

import java.awt.Point;

import finalProject1.FinalProject;
import finalProject1.board.Board;
import javafx.scene.image.ImageView;

public abstract class Entity {
	private static EntityManager manager=new EntityManager();//this stores all the entities
	private ImageView img;
	protected int x, y;// the entities location in tiles
	
	/**
	 * 
	 * @param project - what the entity will be added to
	 * @param img - an image that is what the entity will look like
	 */
	public Entity(FinalProject project, ImageView img) {
		this.img=img;
		project.add(img);
		manager.addEntity(this);//adding itself to the entitymanager
	}
	
	public void update() {
		Point loc= Board.tilesToPixels(x, y);
		img.setX(loc.x);
		img.setY(loc.y);
	}
	
	//getters/setters
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public static EntityManager getManager() {
		return manager;
	}
}
