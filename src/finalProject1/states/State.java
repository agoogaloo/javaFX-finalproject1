package finalProject1.states;

public abstract class State {
	private static State currentState;	
	
	abstract public void start();
	abstract public void update();
	abstract public void end();
	
	
	public static void setCurrentState(State newState) {
		if(currentState!=null) {
			currentState.end();
		}
		currentState = newState;
		currentState.start();
	}
	public static State getCurrentState() {
		return currentState;
	}

}
