package finalProject1.board;

import java.awt.Point;

import finalProject1.FinalProject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Board {
	//this just draws the board but it could be changed fairly easily to allow for different terrains
	
	//board constants that are needed to convert pixel locations into tiles
	public static final int TILE_WIDTH=31, TILE_HEIGHT=23, OFFSET_X=95,OFFSET_Y=50;
	public static final int WIDTH=7,HEIGHT=7;
	
	private ImageView[][] tiles = new ImageView[WIDTH][HEIGHT];
	private Image highlightTile= new Image("res/tileSelect.png",(TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	private Image tilePic = new Image("res/board.png",(TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public Board(FinalProject project) {
		for(int i=0;i<HEIGHT;i++) {
			for(int j=0;j<WIDTH;j++) {
				ImageView tile =new ImageView(tilePic);
				Point location=tilesToPixels(j, i);
				
				
				tile.setX(location.x);
				tile.setY(location.y);
				project.add(tile);
				tiles[j][i]=tile;
			}
		}
		
	}
	
	public void highlightTile(int x, int y) {
		if(x>=0&&y>=0&&x<WIDTH&&y<HEIGHT)
			tiles[x][y].setImage(highlightTile);
		
	}
	public void deHighlight() {
		for(int i=0;i<tiles.length;i++) {
			for(int j=0;j<tiles[0].length;j++) {
				tiles[i][j].setImage(tilePic);
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
		x=((x)/FinalProject.PIXEL_SCALE-OFFSET_X)/TILE_WIDTH;
		y=((y)/FinalProject.PIXEL_SCALE-OFFSET_Y)/TILE_HEIGHT;
		return new Point(x,y);
	}	
}
