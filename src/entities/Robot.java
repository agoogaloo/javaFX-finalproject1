package entities;

import finalProject1.FinalProject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Robot extends Entity{
	final static int IMG_WIDTH=32;
	
	public Robot(FinalProject project) {
		super(project,new ImageView( new Image("res/heliBot.gif",(IMG_WIDTH)*FinalProject.PIXEL_SCALE,0,true,false)));
		x=0;
		y=0;
	}
	public void move(int x, int y) {
		this.x=x;
		this.y=y;
	}

}
