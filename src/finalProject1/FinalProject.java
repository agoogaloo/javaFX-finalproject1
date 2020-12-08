package finalProject1;

import java.util.concurrent.ThreadLocalRandom;

import entities.Entity;
import entities.Robot;
import finalProject1.board.Board;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FinalProject extends Application{

	public static final int PIXEL_SCALE = 3;
	private Group root;
	private Robot rob;
	private int time=0;
	
	
	Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(1), event->{
		time++;
		if(time%120==0) {
			rob.move(ThreadLocalRandom.current().nextInt(0,7),ThreadLocalRandom.current().nextInt(0,7));
		}
		Entity.getManager().update();
		
	}));
	
	public static void main(String[] args) {
		launch();	
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("a super cool fancy title");
		root=new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Canvas canvas = new Canvas(600, 400);
		canvas.setScaleX(PIXEL_SCALE);
		canvas.setScaleY(PIXEL_SCALE);
		add(canvas);
		stage.show();
		root.requestFocus();
		run();
		
	}
	
	
	private void run() {
		Rectangle bg = new Rectangle(0, 0, 600*PIXEL_SCALE, 300*PIXEL_SCALE);
		bg.setFill(new Color(0.1,0.1,0.1,1));//filling in the background
		add(bg);
		new Board(this,7, 7);
		rob=new Robot(this);
		
		timeLine.setCycleCount(Animation.INDEFINITE);
		timeLine.play();
		timeLine.setRate(60);
	}
	
	public void add(Node node){
		root.getChildren().add(node); 
	}

}
