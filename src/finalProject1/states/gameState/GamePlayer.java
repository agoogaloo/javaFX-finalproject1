package finalProject1.states.gameState;

import finalProject1.FinalProject;

public abstract class GamePlayer {
	protected boolean doneTurn=true;
	protected int money;
	protected int playerNum;
	protected FinalProject project;
	
	public GamePlayer(FinalProject project) {
		this.project=project;
	}
	
	public abstract void update();
	
	public void startTurn() {
		doneTurn=false;
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
