package finalProject1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import finalProject1.board.Board;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class Assets {

	public static final Image tank = new Image("res/the tank.gif",33*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image heliBot = new Image("res/heliBot.gif",31*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tower = new Image("res/tower.png",32*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tile = new Image("res/tile.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tileSelect = new Image("res/tileSelect.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tileHighlight = new Image("res/tileHighlight.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Font boldfont = loadFont("src/res/boldfont.ttf");
	public static final Font font = loadFont("src/res/font.ttf");
	public static final Image cardBack= new Image("res/card back.png",32*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image cardFront= new Image("res/card front.png",38*FinalProject.PIXEL_SCALE,0,true,false);;
	
	
	private static Font loadFont(String path) {
		try {
			return Font.loadFont(new FileInputStream(new File(path)),48);
		} catch (FileNotFoundException e) {
			System.out.println("couldnt find a font at '"+path+"'");
			e.printStackTrace();
		}
		return null;
	}
	
}
