package finalProject1;

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
	
	Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(1), event->{
		
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
		bg.setFill(new Color(0.2,0.2,0.2,1));
		add(bg);
		new Board(this, 95, 50, 7, 7);
		
		timeLine.setCycleCount(Animation.INDEFINITE);
		timeLine.play();
		timeLine.setRate(60);
	}
	
	public void add(Node node){
		root.getChildren().add(node); 
	}

}
