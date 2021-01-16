package finalProject1.states;

import java.io.IOException;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.network.NetworkData;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * this is the win/lose screen that appears when the game ends
 * @author aguy2
 *
 */
public class GameEnd extends State{

	private Group quitButton = new Group(), restartButton=new Group();
	Text title;
	FinalProject project;
	NetworkData opponent;
	
	public GameEnd(FinalProject project, NetworkData opponent,Boolean winner) {
		this.project=project;
		this.opponent=opponent;
		//creating the title text boxwith default text
		title=new Text(160*FinalProject.PIXEL_SCALE, 50*FinalProject.PIXEL_SCALE,"did you win????");
		
		//making the quit button
		Text quitText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "QUIT GAME");
		quitText.setFill(Color.WHITE);
		quitText.setFont(Assets.boldfont);
		//putting it in the right place and adding the background
		quitButton.setTranslateX(100*FinalProject.PIXEL_SCALE);
		quitButton.setTranslateY(150*FinalProject.PIXEL_SCALE);
		quitButton.getChildren().add(new Rectangle(100*FinalProject.PIXEL_SCALE,
				30*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		quitButton.getChildren().add(quitText);//adding the text
		
		//making the restart button
		Text restartText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "REMATCH");
		restartText.setFill(Color.WHITE);
		restartText.setFont(Assets.boldfont);
		//putting it in the right place and adding the background
		restartButton.setTranslateX(250*FinalProject.PIXEL_SCALE);
		restartButton.setTranslateY(150*FinalProject.PIXEL_SCALE);
		restartButton.getChildren().add(new Rectangle(100*FinalProject.PIXEL_SCALE,
				30*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		restartButton.getChildren().add(restartText);//adding the text
		
		//the title text font/colour
		title.setFill(Color.WHITE);
		title.setFont(Assets.boldfont);
		
		//saying whether you won or lost
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
		//restarting if the restart button is pressed
		if(restartButton.isPressed()) {
			try {
				State.setCurrentState(new ConnectionState(project, opponent.getIp()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		//quitting to the main menu if quit is pressed
		}else if(quitButton.isPressed()) {
			State.setCurrentState(new MainMenu(project));
		}
	}

	@Override
	public void end() {
		project.clear();
	}

}
