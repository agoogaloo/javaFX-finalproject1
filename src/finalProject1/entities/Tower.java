package finalProject1.entities;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.image.ImageView;

public class Tower extends Entity{

	
	public Tower(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.tower),player);
		health=1;
		this.x=x;
		this.y=y;
		setyOffset(-6);
		
	}
}
