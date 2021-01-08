package finalProject1;

import finalProject1.states.MainMenu;
import finalProject1.states.State;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * this is the main class that launches the game
 * @author
 *
 */
public class FinalProject extends Application{
	//how much the pixel art is scaled up. i cant just scale the entire thing at once
	//because it will use antialias and ruin the pixel art
	public static final int PIXEL_SCALE = 3;
	private Group root;//the group that holds everything in the game 
	//thie timeline that updated the game 60 times a second
	Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(1), event->{
		//updating the game and then the inputs
		State.getCurrentState().update();
		Inputs.update();
	}));
	
	//the main method that launches the game
	public static void main(String[] args) {
		launch();	
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		//initializing everything
		//setting the title
		stage.setTitle("a super cool fancy title");
		root=new Group();
		//resizing the window could break the ui so i disable it
		stage.setResizable(false);
		Scene scene = new Scene(root);//creating a new scene
		stage.setScene(scene);//adding the scene so we can see it
		Canvas canvas = new Canvas(450*PIXEL_SCALE, 260*PIXEL_SCALE);//making the canvas the right size
		add(canvas);//adding the canvas so its usable
		stage.show();//showing things so its not invisible
		root.requestFocus();
		run();	//running the game
	}
	
	
	private void run() {
		//creating a rectangle for the background
		clear();
		root.setOnMousePressed(Inputs.getMouseClick());
		root.setOnMouseMoved(Inputs.mouseMove);
		//initializing the gamestate
		State.setCurrentState(new MainMenu(this));
		 
		timeLine.setCycleCount(Animation.INDEFINITE);//making it run forever
		timeLine.play();//starting the timeline
		timeLine.setRate(60);//setting the framerate
	}
	//these methods let you add or remove things from root, adding or removing them from the game
	public void add(Node node){
		root.getChildren().add(node); 
	}
	public void remove(Node node) {
		root.getChildren().remove(node);
	}
	public void clear() {
		root.getChildren().clear();	
		Rectangle bg = new Rectangle(0, 0, 450*PIXEL_SCALE, 260*PIXEL_SCALE);
		bg.setFill(new Color(0.1,0.1,0.1,1));//making the background the right colour
		add(bg);//adding the background so its visible
		//bg.toBack();		
	}

}
