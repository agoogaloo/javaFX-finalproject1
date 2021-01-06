package finalProject1.states;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.states.gameState.GameState;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MainMenu extends State{

	TextField ip = new TextField("enter ip here");
	private Group playButton = new Group();//this is the button on the bottom that lets you end your turn
	Text info = new Text(80*FinalProject.PIXEL_SCALE, 100*FinalProject.PIXEL_SCALE, 
			"TYPE 'SERVER' TO MAKE A SERVER, OR YOUR OPPONENTS IP ADDRESS TO JOIN THEM"),
			title = new Text(160*FinalProject.PIXEL_SCALE, 50*FinalProject.PIXEL_SCALE,"ROBOT FIGHT SUPREME");
	FinalProject project;
	public MainMenu(FinalProject project) {
		this.project=project;
		//setting properties for the text box
		ip.setPrefWidth(100*FinalProject.PIXEL_SCALE);
		ip.setPrefHeight(10*FinalProject.PIXEL_SCALE);
		ip.setLayoutX(175*FinalProject.PIXEL_SCALE);
		ip.setLayoutY(110*FinalProject.PIXEL_SCALE);
		
		//the play button
		Text endTurnText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "START GAME");
		endTurnText.setFill(Color.WHITE);
		endTurnText.setFont(Assets.boldfont);
		//moving the endturn button to the right spot and adding the graphics to it
		playButton.setTranslateX(175*FinalProject.PIXEL_SCALE);
		playButton.setTranslateY(150*FinalProject.PIXEL_SCALE);
		playButton.getChildren().add(new Rectangle(100*FinalProject.PIXEL_SCALE,
				30*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		playButton.getChildren().add(endTurnText);
		
		//the instruction text
		info.setFill(Color.WHITE);
		info.setFont(Assets.font);
		//the title text
		title.setFill(Color.WHITE);
		title.setFont(Assets.boldfont);
		
		//adding everything
		project.add(playButton);
		project.add(ip);
		project.add(info);
		project.add(title);
	}
	
	@Override
	public void update() {
		if(playButton.isPressed()) {
			State.setCurrentState(new GameState(project));
		}
	}

	@Override
	public void end() {
		project.remove(ip);
		project.remove(playButton);
		project.remove(info);
		project.remove(title);
	}

}
