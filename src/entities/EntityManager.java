package entities;

import java.util.ArrayList;

public class EntityManager {
	ArrayList<Entity> entities = new ArrayList<>();
	
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void update() {
		for(Entity e: entities)
			e.update();
	}
	
	public Entity getEntity(int x, int y){
		for(Entity i: entities) {
			if(i.getX()==x&&i.getY()==y)
				return i;
		}
		
		return null;
	}

}
