package be.vilevar.dungeon;

import java.util.List;

import org.bukkit.entity.Entity;

@FunctionalInterface
public interface ISpawner {

	/**
	 * 
	 * @param location
	 * @param hall
	 * @return List of bukkit entities. It is recommended to use DungeonEntities in the package be.vilevar.dungeon.entity
	 * @see be.vilevar.dungeon.entity.IDungeonEntity
	 * @see be.vilevar.dungeon.entity.DungeonEntityManager
	 */
	List<Entity> spawn(Hall hall);
}
