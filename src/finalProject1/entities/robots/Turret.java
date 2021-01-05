package finalProject1.entities.robots;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.image.ImageView;

public class Turret extends Robot{
	public static final int ID=3, MAXHEALTH=4, COST=2;
	
	public Turret(FinalProject project,int player) {
		this(project,player, 0, 0);
	}
	public Turret(FinalProject project, int player, int x, int y) {
		super(project,new ImageView(Assets.turret),player,x,y);
		description ="a turret that can't move, but can shoot quite far";
		health=MAXHEALTH;
		range=4;
		damage=2;
		speed=0;
	}
	
	@Override
	public void startTurn() {
		moved=true;//the turret cant move so when it starts its turn it shouldnt be able to move
		attacked=false;
	}
}
