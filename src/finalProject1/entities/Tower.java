package finalProject1.entities;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.image.ImageView;

public class Tower extends Entity{
	private static final int ID=0;
	
	public Tower(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.tower),player);//giving it the right player picture and an ID of 0
		description="make sure to keep these towers safe. \nIf you loose all your towers, you lose the game";
		health=1;
		this.x=x;
		this.y=y;
		setyOffset(-6);
		
	}
	
	@Override
	public void showInfo() {
		super.showInfo();
		infoText.setText("HP: "+ health+"\n"+description.toUpperCase());
		
		
	}
	public static int getID() {
		return ID;
	}
}
