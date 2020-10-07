package be.vilevar.dungeon.entity;

import org.bukkit.entity.Player;

import be.vilevar.dungeon.Dungeon;
import be.vilevar.dungeon.Hall;

public interface IDungeonEntity {

	void setHall(Hall hall);
	Hall getHall();
	
	default boolean canInteractWith(Player p) {
		Dungeon d = this.getHall().getDungeon();
		return d.hasBegon() && d.getAlivePlayers().contains(p);
	}
}
