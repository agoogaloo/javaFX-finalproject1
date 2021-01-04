package finalProject1.entities.robots;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.image.ImageView;

public class Tank extends Robot{
	public static final int ID=2, MAXHEALTH=3, COST=2,YOFFSET=-6,XOFFSET=2;
	
	public Tank(FinalProject project,int player) {
		this(project,player, 0, 0);
	}
	public Tank(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.tank),player,x,y);
		description ="a slow robot that doesnt deal much damage, but can suvive a lot of hits";
		health=MAXHEALTH;
		range=3;
		damage=1;
		speed=2;
		setyOffset(YOFFSET);
		setxOffset(XOFFSET);
	}

}
