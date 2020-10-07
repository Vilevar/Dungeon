package be.vilevar.dungeon.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import be.vilevar.dungeon.Hall;

public class SpawnerFileManager {

	protected Random random = new Random();
	protected Thread[] threads;
	
	public SpawnerFileManager(Thread[] threads) {
		this.threads = threads;
	}
	
	public List<Entity> spawn(Hall hall) {
		ArrayList<Entity> entities = new ArrayList<>();
		for(Thread thread : this.threads) {
			if(this.random.nextDouble() > thread.probability)
				continue;
			for(Entry<EntityFileManager, Integer> entry : thread.entities.entrySet()) {
				for(int i = 0; i < entry.getValue(); i++) {
					Entity ent = entry.getKey().spawn(hall, thread.loc);
					if(thread.register)
						entities.add(ent);
				}
			}
			
		}
		return entities;
	}
	
	
	public static class Thread {
		
		protected float probability;
		protected Location loc;
		protected HashMap<EntityFileManager, Integer> entities;
		protected boolean register;
		
		public Thread(float probability, Location loc, HashMap<EntityFileManager, Integer> entities, boolean register) {
			this.probability = probability;
			this.loc = loc;
			this.entities = entities;
			this.register = register;
		}
	}
}
