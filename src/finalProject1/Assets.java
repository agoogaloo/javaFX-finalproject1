package finalProject1;

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
	public static final Image bomb = new Image("res/bomb.gif",32*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tile = new Image("res/tile.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tileSelect = new Image("res/tileSelect.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image tileHighlight = new Image("res/tileHighlight.png",(Board.TILE_WIDTH+1)*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Font boldfont = loadFont("/res/boldfont.ttf");
	public static final Font font = loadFont("/res/font.ttf");
	public static final Image cardBack= new Image("res/card back.png",38*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image cardFront= new Image("res/card front.png",38*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image deck= new Image("res/deck.png",33*FinalProject.PIXEL_SCALE,0,true,false);
	public static final Image arrow= new Image("res/selection.png",5*FinalProject.PIXEL_SCALE,0,true,false);
	
	
	/**
	 * this loads a font at a specific path or returns null if it doesnt exist
	 * it was more usefull when i loaded things as a file because it needed a try catch, 
	 * but it still save writing a long line of code
	 * @param path - a string holding the path to the font
	 * @return - the loaded font or null if it cant be fount at the path
	 */
	private static Font loadFont(String path) {	
			return Font.loadFont(Assets.class.getClass().getResourceAsStream(path),48);
			
			/*if you are using java 15 loading assets as a resource doesnt work right for some reason so you can use this way to load fonts instead
			 * try {//trying to load the font
				return Font.loadFont(new FileInputStream(new File(path)),48);
			} catch (FileNotFoundException e) {//saying it couldnt find the font if it couldnt find the font
				System.out.println("couldnt find a font at '"+path+"'");
				e.printStackTrace();
			}
			return null;
			 */
		
		
		
	}
	
}
