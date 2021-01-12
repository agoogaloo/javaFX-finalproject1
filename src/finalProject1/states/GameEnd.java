package finalProject1.states;

import java.io.IOException;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.network.NetworkData;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameEnd extends State{

	private Group quitButton = new Group(), restartButton=new Group();
	Text title;
	FinalProject project;
	NetworkData opponent;
	public GameEnd(FinalProject project, NetworkData opponent,Boolean winner) {
		this.project=project;
		this.opponent=opponent;
		title=new Text(160*FinalProject.PIXEL_SCALE, 50*FinalProject.PIXEL_SCALE,"did you win????");
		
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
		
		Text restartText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "REMATCH");
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
		
		if(winner) {
			title.setText("YOU WIN!");
		}else {
			title.setText("YOU LOST");
		}
		
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
			try {
				State.setCurrentState(new ConnectionState(project, opponent.getIp()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(quitButton.isPressed()) {
			State.setCurrentState(new MainMenu(project));
		}
	}

	@Override
	public void end() {
		project.clear();
	}

}
