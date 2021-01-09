package finalProject1.states.gameState;

public abstract class GamePlayer {
	protected boolean doneTurn;
	protected int money;
	//this holds where that are moving/attacking and what robot that are using
	int[] move= {-1,-1,-1,-1}, attack= {-1,-1,-1,-1};
	
	
	public GamePlayer() {
		
	}
	
	public abstract void update();
	
	
	public int[] getMove() {
		int[]value=move;
		move= new int[]{-1,-1,-1,-1};
		return value;
	}
	public int[] getAttack() {
		int[]value=attack;
		attack= new int[]{-1,-1,-1,-1};
		return value;
	}
}
