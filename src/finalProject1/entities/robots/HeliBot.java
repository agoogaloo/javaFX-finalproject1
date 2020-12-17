package finalProject1.entities.robots;

import finalProject1.FinalProject;
import finalProject1.entities.Assets;
import javafx.scene.image.ImageView;

public class HeliBot extends Robot{
	private final static int IMG_WIDTH=32;
	
	public HeliBot(FinalProject project,int player) {
		this(project,player, 0, 0);
	}
	
	public HeliBot(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.heliBot),player,x,y);
		health=1;
		speed=3;
		damage=2;
		range=3;
		
	}

}
