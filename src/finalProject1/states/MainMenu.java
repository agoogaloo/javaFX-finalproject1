package finalProject1.states;

import java.io.IOException;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.states.gameState.GameState;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MainMenu extends State{

	private TextField ip = new TextField("enter ip here");//this holds the ip address as a textbox
	private Group playButton = new Group();//this is the button on the bottom that lets you start the game
	//this lets you know how to start the game
	private Text info = new Text(80*FinalProject.PIXEL_SCALE, 100*FinalProject.PIXEL_SCALE, 
			"TYPE 'SERVER' TO MAKE A SERVER, OR YOUR OPPONENTS IP ADDRESS TO JOIN THEM"),
			//thie title
			title = new Text(160*FinalProject.PIXEL_SCALE, 50*FinalProject.PIXEL_SCALE,"ROBOT FIGHT SUPREME");
	private FinalProject project;
	public MainMenu(FinalProject project) {
		this.project=project;
		//setting properties for the text input box
		ip.setPrefWidth(100*FinalProject.PIXEL_SCALE);
		ip.setPrefHeight(10*FinalProject.PIXEL_SCALE);
		ip.setLayoutX(175*FinalProject.PIXEL_SCALE);
		ip.setLayoutY(110*FinalProject.PIXEL_SCALE);
		
		//the play button
		Text startText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "START GAME");
		startText.setFill(Color.WHITE);
		startText.setFont(Assets.boldfont);
		//moving the start button to the right spot and adding the text to it
		playButton.setTranslateX(175*FinalProject.PIXEL_SCALE);
		playButton.setTranslateY(150*FinalProject.PIXEL_SCALE);
		playButton.getChildren().add(new Rectangle(100*FinalProject.PIXEL_SCALE,
				30*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		playButton.getChildren().add(startText);
		
		//the instruction text
		info.setFill(Color.WHITE);
		info.setFont(Assets.font);
		//the title text
		title.setFill(Color.WHITE);
		title.setFont(Assets.boldfont);		
	}
	
	@Override
	public void start() {
		//adding everything to the game
		project.add(playButton);
		project.add(ip);
		project.add(info);
		project.add(title);
	}
	
	@Override
	public void update() {
		//trying to start the game when you push the start button
		if(playButton.isPressed()) {
			ConnectionState state;
			
			try {//creating a connection state can throw errors so it needs to be in a try catch
				//creating the state
				state = new ConnectionState(project,ip.getText());
			} catch (IOException e) {
				//if it couldnt be created it just exits the method and shows the error
				System.out.println("an error occured while doing online things");
				e.printStackTrace();
				return;
			}
			//if it has succeded it will set the state to the connection state and let us know who we connected to
			State.setCurrentState(state);
			System.out.println("started connection with ip:"+ip.getText());
		}
	}

	@Override
	public void end() {
		//clearing everything off the screen so that it wont stay there for the next state
		project.clear();
	}

	

}
