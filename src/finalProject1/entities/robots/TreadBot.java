package finalProject1.entities.robots;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.image.ImageView;

public class TreadBot extends Robot{
	public static final int ID=4, MAXHEALTH=1, COST=1,YOFFSET=-3;
	
	public TreadBot(FinalProject project,int player) {
		this(project,player, 0, 0);
	}
	public TreadBot(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.treadBot),player,x,y);
		description ="a weak, cheap robot. it's not that good but its cheap";
		health=MAXHEALTH;
		range=2;
		damage=1;
		speed=1;
		setyOffset(YOFFSET);
	}

}
