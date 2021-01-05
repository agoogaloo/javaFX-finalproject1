package finalProject1.entities.robots;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.image.ImageView;

public class HeliBot extends Robot{
	public static final int ID=1, MAXHEALTH=1,COST=6;
	
	public HeliBot(FinalProject project,int player) {
		this(project,player, 0, 0);
	}
	
	public HeliBot(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.heliBot),player,x,y);
		description = "the ghost of the best robot ever";
		health=MAXHEALTH;
		speed=5;
		damage=9;
		range=9;
		
	}
}
