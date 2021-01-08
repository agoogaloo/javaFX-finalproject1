package finalProject1.states;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.states.gameState.GameState;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameEnd extends State{

	private Group quitButton = new Group(), restartButton=new Group();
	Text title;
	FinalProject project;
	public GameEnd(FinalProject project, int winner) {
		this.project=project;
		title=new Text(160*FinalProject.PIXEL_SCALE, 50*FinalProject.PIXEL_SCALE,"PLAYER"+winner+"WINS");
		
		//the play button
		Text quitText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "QUIT GAME");
		quitText.setFill(Color.WHITE);
		quitText.setFont(Assets.boldfont);
		//moving the endturn button to the right spot and adding the graphics to it
		quitButton.setTranslateX(100*FinalProject.PIXEL_SCALE);
		quitButton.setTranslateY(150*FinalProject.PIXEL_SCALE);
		quitButton.getChildren().add(new Rectangle(100*FinalProject.PIXEL_SCALE,
				30*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		quitButton.getChildren().add(quitText);
		
		Text restartText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "NEW GAME");
		restartText.setFill(Color.WHITE);
		restartText.setFont(Assets.boldfont);
		
		restartButton.setTranslateX(250*FinalProject.PIXEL_SCALE);
		restartButton.setTranslateY(150*FinalProject.PIXEL_SCALE);
		restartButton.getChildren().add(new Rectangle(100*FinalProject.PIXEL_SCALE,
				30*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		restartButton.getChildren().add(restartText);
		
		//the title text
		title.setFill(Color.WHITE);
		title.setFont(Assets.boldfont);
		
		
	}
	
	@Override
	public void start() {
		//adding everything
		project.add(quitButton);
		project.add(restartButton);
		project.add(title);
	}
	
	@Override
	public void update() {
		if(restartButton.isPressed()) {
			State.setCurrentState(new GameState(project));
		}else if(quitButton.isPressed()) {
			State.setCurrentState(new MainMenu(project));
		}
	}

	@Override
	public void end() {
		project.clear();
	}

}
