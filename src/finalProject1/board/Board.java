package finalProject1.board;

import finalProject1.FinalProject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Board {
	public final int TILE_WIDTH=31, TILE_HEIGHT=23;
	
	public Board(FinalProject project, int x, int y, int width, int height) {
		Image img = new Image("res/board.png",(TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
		for(int i=0;i<height*TILE_HEIGHT;i+=TILE_HEIGHT) {
			for(int j=0;j<width*TILE_WIDTH;j+=TILE_WIDTH) {
				ImageView tile =new ImageView(img);
				
				
				
				tile.setX((x+(j))*FinalProject.PIXEL_SCALE);
				tile.setY((y+(i))*FinalProject.PIXEL_SCALE);
				project.add(tile);
			}
		}
	}
}
