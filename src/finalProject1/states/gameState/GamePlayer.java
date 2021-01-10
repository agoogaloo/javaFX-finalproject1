package finalProject1.states.gameState;

import finalProject1.Assets;
import finalProject1.FinalProject;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class GamePlayer {
	protected boolean doneTurn=true;
	protected int money=5;
	protected int playerNum;
	protected FinalProject project;
	
	protected Text moneyText = new Text(195*FinalProject.PIXEL_SCALE,10*FinalProject.PIXEL_SCALE,
			"this should say your money");
	
	public GamePlayer(FinalProject project) {
		this.project=project;
		
		moneyText.setFill(Color.WHITE);
		moneyText.setFont(Assets.font);
		project.add(moneyText);
	}
	
	public abstract void update();
	
	
	public void startTurn() {
		doneTurn=false;
		//money+=2;
	}
	public int getMoney() {
		return money;
	}
	public boolean isDoneTurn() {
		return doneTurn;
	}
	public int getPlayerNum() {
		return playerNum;
	}
	
	
}
