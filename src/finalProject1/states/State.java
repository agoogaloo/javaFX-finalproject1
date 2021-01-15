package finalProject1.states;

/**
 * States are for the different ways or places that the game can be in, so there would be a different 
 * states for different menus, pause screens, or when you are actually playing the game
 *
 */
public abstract class State {
	//this is the state that the game is currently in
	private static State currentState;	
	
	//this is called after the screen has been cleared and the state is ready to start doing its thing
	abstract public void start();
	//called once a frame. this is where everything actually happens
	abstract public void update();
	//this lets us stop things once the state is changed, and close everything that should be closed
	abstract public void end();
	
	
	/**
	 * this changes the current state to something else, ending the lod one and starting the new one.
	 * @param newState
	 */
	public static void setCurrentState(State newState) {
		if(currentState!=null) {//making sure that there is a state to end
			currentState.end();//ending the old state
		}
		
		currentState = newState;//setting the new state to be the active state
		currentState.start();//starts the new state so it can do whatever it needs to get ready
	}
	
	/**
	 * @return - the active state
	 */
	public static State getCurrentState() {
		return currentState;
	}

}
