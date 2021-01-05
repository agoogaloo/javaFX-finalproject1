package finalProject1.entities;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.image.ImageView;

/**
 * the towers you are supposed to protect
 * @param project - the project it is being added to
 * @param player - the player the tower belongs to
 * @param x - the x coordinate in tiles
 * @param y - the y coordinate in tiles
 */
public class Tower extends Entity{
	private static final int ID=0;///giving it an id
	
	public Tower(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.tower),player);//giving it the right player picture
		description="make sure to keep these towers safe. \nIf you loose all your towers, you lose the game";
		health=3;
		this.x=x;
		this.y=y;
		setyOffset(-6);
		
	}
	public static int getID() {
		return ID;
	}
}
