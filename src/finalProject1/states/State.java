package finalProject1.states;

public abstract class State {
	private static State currentState;	
	
	abstract public void update();
	abstract public void end();
	
	
	public static void setCurrentState(State currentState) {
		if(State.currentState!=null)
			State.currentState.end();
		State.currentState = currentState;
	}
	public static State getCurrentState() {
		return currentState;
	}

}
