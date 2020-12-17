package finalProject1;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Inputs {
	
	private Inputs() {//making it so you can make an instance of the inputs because that wouldnt make much sence
		
	}
	private static int x,y;
	private static boolean clicked=false;
	private static EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			clicked=true;
			
		}
	};
	static EventHandler<MouseEvent> mouseMove = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			x=(int) event.getX();
			y=(int) event.getY();
		}
	};
	
	static public void update() {
		clicked=false;
	}
	
	//getters
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
