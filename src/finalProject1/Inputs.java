package finalProject1;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * this class makes it possible to get inputs from anywhere in the game
 *
 */
public class Inputs {
	//making a private constructor so that you cant make an instance of the class because that 
	//wouldnt make much sence because everything is static
	
	private Inputs() {
		
	}
	
	private static int x,y;//the location of the mouse in pixels
	private static boolean clicked=false;//whether the mouse has been clicked this frame
	//this make it so that clicked is set to true whenever the mouse is clicked
	private static EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			clicked=true;
			
		}
	};
	//this make sets the x and y variable to be at the mouses location whenever it moves
	static EventHandler<MouseEvent> mouseMove = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			x=(int) event.getX();
			y=(int) event.getY();
		}
	};
	
	static public void update() {
		//setting clicked back to false once the frame has finished so that it wont stay pressed for more than 1 frame
		clicked=false;
	}
	
	//getters for the mouses properties
	public static int getX() {
		return x;
	}
	public static int getY() {
		return y;
	}
	public static EventHandler<MouseEvent> getMouseClick() {
		return mouseClick;
	}
	public static EventHandler<MouseEvent> getMouseMove() {
		return mouseMove;
	}
	public static boolean isClicked() {
		return clicked;
	}
}
