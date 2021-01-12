package finalProject1.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityManager {
	//ArrayList<Entity> entities = new ArrayList<>();
	private ArrayList<Entity>player1=  new ArrayList<Entity>(), player2=  new ArrayList<Entity>();

	public EntityManager() {
	}
	
	public void addEntity(Entity e){
	
		if(e.getPlayer()==1)
			player1.add(e);
		if(e.getPlayer()==2)
			player2.add(e);
	}
	
	public void update() {
		for(int i=0;i<player1.size();i++) {
			player1.get(i).update();
			if(!player1.get(i).isAlive()){
				player1.remove(i);
			}
		}
		for(int i=0;i<player2.size();i++) {
			player2.get(i).update();
			if(!player2.get(i).isAlive()){
				player2.remove(i);
			}
		}
	}
	
	public Entity getEntity(int x, int y){
		for(Entity i: player1) {
			if(i.getX()==x&&i.getY()==y) {
				return i;
			}
			
		}
		for(Entity i: player2) {
			if(i.getX()==x&&i.getY()==y) {
				return i;
			}
			
		}
		
		
		return null;
	}
	public ArrayList<Entity> getEntities(int player) {
		if(player==1)
			return player1;
		if(player==2)
			return player2;
		return null;
	}
	
	public ArrayList<Entity> getPlayer1() {
		return player1;
	}
	
	public ArrayList<Entity> getPlayer2() {
		return player2;
	}
	public void reset() {
		player1.clear();
		player2.clear();
	}

}
