package finalProject1.entities.robots;

import finalProject1.FinalProject;
import finalProject1.entities.Assets;
import javafx.scene.image.ImageView;

public class Tank extends Robot{
	private final static int IMG_WIDTH=33;
	
	public Tank(FinalProject project,int player) {
		this(project,player, 0, 0);
	}
	public Tank(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.tank),player,x,y);
		health=3;
		range=3;
		damage=1;
		speed=2;
		setyOffset(-6);
		setxOffset(2);
	}

}
