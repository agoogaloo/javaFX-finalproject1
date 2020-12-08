package finalProject1.board;

import java.awt.Point;

import finalProject1.FinalProject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Board {
	//this just draws the board but it could be changed fairly easily to allow for different terrains
	
	//board constants that are needed to convert pixel locations into tiles
	public static final int TILE_WIDTH=31, TILE_HEIGHT=23, OFFSET_X=95,OFFSET_Y=50;
	
	public Board(FinalProject project, int width, int height) {
		Image img = new Image("res/board.png",(TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				ImageView tile =new ImageView(img);
				Point location=tilesToPixels(j, i);
				
				
				tile.setX(location.x);
				tile.setY(location.y);
				project.add(tile);
			}
		}
		
	}
	
	/**
	 * this method converts a location in tiles to where it will be rendered on the screen in pixels
	 * @param x - x location in tiles
	 * @param y - y location in tiles
	 * @return - a point object that has the x and y of the location given in pixels  
	 */
	public static Point tilesToPixels(int x, int y) {
		x= FinalProject.PIXEL_SCALE*(x*TILE_WIDTH+OFFSET_X);
		y=FinalProject.PIXEL_SCALE*(y*TILE_HEIGHT+OFFSET_Y);
		return new Point(x,y);
	}
	
	/**
	 * this method converts a location of something on the screen to where it will be on the board in tiles
	 * @param x - x location in tiles
	 * @param y - y location in tiles
	 * @return - a point object that has the x and y of the location given in pixels  
	 */
	public static Point PixelsToTiles(int x, int y) {
		x=(x/FinalProject.PIXEL_SCALE-OFFSET_X)/TILE_WIDTH;
		y=(y/FinalProject.PIXEL_SCALE-OFFSET_Y)/TILE_HEIGHT;
		return new Point(FinalProject.PIXEL_SCALE*(x),FinalProject.PIXEL_SCALE*(y));
	}
	
}
