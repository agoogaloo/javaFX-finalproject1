package finalProject1.states.gameState;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * this represents the playrer of the game, whether thats you or your opponent *
 */
public abstract class GamePlayer {
	protected boolean doneTurn=true;//whether the player has finished their turn
	protected int money=5;//their money
	protected int playerNum;//whether their player 1 or 2
	protected FinalProject project;//the project so they can add things to the screen
	
	//this shows how much money they have
	protected Text moneyText = new Text(195*FinalProject.PIXEL_SCALE,10*FinalProject.PIXEL_SCALE,
			"this should say your money");
	
	public GamePlayer(FinalProject project) {
		this.project=project;
		//formatting/adding the text
		moneyText.setFill(Color.WHITE);
		moneyText.setFont(Assets.font);
		project.add(moneyText);
	}
	
	//called every frame to make the player do things
	public abstract void update();
	
	
	//getters/setters
	public void startTurn() {
		System.out.println(playerNum+"starting");
		doneTurn=false;
	}
	public int getMoney() {
		return money;
	}
	public void giveMoney(int amount) {
		money+=amount;
	}
	public boolean isDoneTurn() {
		return doneTurn;
	}
	public int getPlayerNum() {
		return playerNum;
	}
	
	
}
