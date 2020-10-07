package be.vilevar.dungeon;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import com.google.common.collect.ImmutableList;

public class Hall {

	private final int id;
	private final Dungeon dungeon;
	private final IDoor door;
	private final ISpawner spawner;
	private List<Entity> entities;
	
	public Hall(int id, Dungeon dungeon, IDoor door, ISpawner spawner) {
		this.id = id;
		this.dungeon = dungeon;
		this.door = door;
		this.spawner = spawner;
	}

	public int getId() {
		return id;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public IDoor getDoor() {
		return this.door;
	}
	
	public void spawnEntities() {
		if(this.hasBegon())
			throw new IllegalStateException("This hall has already begon");
		Bukkit.getScheduler().runTaskLater(DungeonPlugin.instance, () -> {
			this.entities = this.spawner.spawn(this);
			if(this.entities == null || this.entities.isEmpty()) {
				this.dungeon.toNextState();
				throw new IllegalArgumentException("The given spawner "+this.spawner+" set an empty list or an null list");
			}
		}, 1);
	}
	
	public List<Entity> getEntities() {
		return this.entities==null ? null : ImmutableList.copyOf(this.entities);
	}
	
	/**
	 * @param org.bukkit.entity.Entity
	 * @return true if there is no more entities and false if there are still entities.
	 */
	public boolean removeEntity(Entity ent) {
		if(!this.entities.contains(ent)) 
			throw new IllegalArgumentException("The entity "+ent.getName()+" ("+ent+") is not an entity of this room");
		if(!ent.isDead())
			ent.eject();
		this.entities.remove(ent);
		if(this.entities.isEmpty()) {
			this.entities = null;
			return true;
		}
		return false;
	}
	
	public void killEntities() {
		Iterator<Entity> entities = this.entities.iterator();
		while(entities.hasNext()) {
			Entity ent = entities.next();
			entities.remove();
			if(!ent.isDead())
				ent.remove();
		}
		this.entities = null;
	}
	
	public boolean hasBegon() {
		return this.entities != null;
	}
	
	@Override
	public String toString() {
		return "[Hall(dugeon="+this.dungeon+", id="+this.id+")]";
	}
}
