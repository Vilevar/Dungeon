package be.vilevar.dungeon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;

public interface IDoor {

	default int getTimeOpen() {
		return 45;
	}
	boolean isNextOpen();
	void openNext(boolean open);
	default void openNext(boolean open, int seconds) {
		this.openNext(open);
		if(seconds != -1) {
			Bukkit.getScheduler().runTaskLater(DungeonPlugin.instance, () -> {
				this.openNext(!open);
			}, seconds*20);
		}
	}
	
	public static class DefaultDoor implements IDoor {

		private final Block block;
		private final Openable openable;
		
		public DefaultDoor(Location loc) {
			try {
				this.block = loc.getBlock();
				this.openable = (Openable) this.block.getState().getBlockData();
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("The block at the location "+loc+" has to be an openable : "+loc.getBlock().getType(), e);
			}
		}
		
		@Override
		public boolean isNextOpen() {
			return openable.isOpen();
		}

		@Override
		public void openNext(boolean arg0) {
			openable.setOpen(arg0);
			this.block.setBlockData(this.openable);
		}

	}
}
