package be.vilevar.dungeon;

import org.bukkit.inventory.ItemStack;

public class Reward {

	public final ItemStack is;
	public final double prob;
	
	public Reward(ItemStack is, double prob) {
		this.is = is;
		this.prob = prob;
	}
}
