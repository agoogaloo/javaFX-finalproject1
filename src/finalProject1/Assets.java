package finalProject1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import finalProject1.board.Board;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * this just loads and holds all the pictures and stuff so they dont need to be reloaded a bunch of times 
 *
 */
public class Assets {

	public static final Image tank = new Image("res/the tank.gif",33*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image heliBot = new Image("res/heliBot.gif",31*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image turret = new Image("res/turret.gif",32*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image treadBot = new Image("res/treadBot.gif",32*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tower = new Image("res/tower.png",32*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tile = new Image("res/tile.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tileSelect = new Image("res/tileSelect.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tileHighlight = new Image("res/tileHighlight.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Font boldfont = loadFont("src/res/boldfont.ttf");
	public static final Font font = loadFont("src/res/font.ttf");
	public static final Image cardBack= new Image("res/card back.png",38*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image cardFront= new Image("res/card front.png",38*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image deck= new Image("res/deck.png",33*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image arrow= new Image("res/selection.png",5*FinalProject.PIXEL_SCALE,0,true,false);
	
	/**
	 * this loads a font at a specific path or returns null if it doesnt exist
	 * it is handy so that i dont need to use a try catch whenever i want to make a font
	 * @param path - a string holding the path to the font
	 * @return - the loaded font or null if it cant be fount at the path
	 */
	private static Font loadFont(String path) {
		
		try {//trying to load the font
			return Font.loadFont(new FileInputStream(new File(path)),48);
		} catch (FileNotFoundException e) {//saying it couldnt find the font if it couldnt find the font
			System.out.println("couldnt find a font at '"+path+"'");
			e.printStackTrace();
		}
		//returning null if it wasnt able to return the font
		return null;
	}
	
}
