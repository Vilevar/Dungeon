package be.vilevar.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;

public class Dungeon {

	private final String name;
	private final int minX, maxX, minZ, maxZ;
	private final List<Hall> halls;
	private final List<Reward> rewards;
	private final Location rewardLocation;
	private final Chest chest;
	
	private long start;
	private List<Player> players;
	private List<Player> alivePlayers;
	private int state;
	
	public Dungeon(String name, int minX, int maxX, int minZ, int maxZ, @Nonnull List<Reward> rewards, Location rewardLocation) {
		this.name = name;
		this.minX = Math.min(minX, maxX);
		this.maxX = Math.max(minX, maxX);
		this.minZ = Math.min(minZ, maxZ);
		this.maxZ = Math.max(minZ, maxZ);
		this.halls = new ArrayList<>();
		this.rewards = rewards;
		this.rewardLocation = rewardLocation;
		if(rewardLocation.getBlock().getType() == Material.CHEST)
			this.chest = (Chest) rewardLocation.getBlock().getState();
		else
			throw new IllegalArgumentException("The block at the position "+rewardLocation+" is not a chest : "+rewardLocation.getBlock().getType());
		this.state = -1;
		
	}

	public String getName() {
		return this.name;
	}

	public int getMinX() {
		return this.minX;
	}

	public int getMaxX() {
		return this.maxX;
	}

	public int getMinZ() {
		return this.minZ;
	}

	public int getMaxZ() {
		return this.maxZ;
	}
	
	public boolean isInDungeon(Location loc) {
		return loc.getWorld().equals(rewardLocation.getWorld()) &&
				(this.minX <= loc.getBlockX() && loc.getBlockX() <= this.maxX) && (this.minZ <= loc.getBlockZ() && loc.getBlockZ() <= this.maxZ);
	}

	public List<Hall> getHalls() {
		return ImmutableList.copyOf(this.halls);
	}
	
	public Hall getHall(int id) {
		return this.halls.get(id);
	}
	
	public void addHalls(Hall...halls) {
		this.halls.addAll(Arrays.asList(halls));
	}

	public List<Reward> getRewards() {
		return ImmutableList.copyOf(this.rewards);
	}

	public Location getRewardLocation() {
		return this.rewardLocation;
	}

	public List<Player> getPlayers() {
		return this.players == null ? null : ImmutableList.copyOf(this.players);
	}

	public List<Player> getAlivePlayers() {
		return this.alivePlayers == null ? null : ImmutableList.copyOf(this.alivePlayers);
	}
	
	public int getNPlayers() {
		return this.players == null ? 0 : this.players.size();
	}
	
	public int getNAlivePlayers() {
		return this.alivePlayers == null ? 0 : this.alivePlayers.size();
	}

	public int getState() {
		return this.state;
	}
	
	public int getGameStates() {
		return this.halls.size();
	}
	
	public int getNStates() {
		return this.getGameStates() + 1;
	}
	
	public boolean isLastGameState() {
		return this.state == this.getGameStates() - 1;
	}
	
	public boolean isGameFinished() {
		return this.state == this.getNStates() - 1;
	}
	
	public boolean hasBegon() {
		return this.state != -1 && this.players != null && this.alivePlayers != null;
	}
	
	public void start(List<Player> players) {
		if(this.hasBegon()) throw new IllegalStateException("The dungeon is already started");
		if(this.getGameStates()==0) throw new IllegalStateException("The dungeon has no hall");
		this.start = System.currentTimeMillis();
		this.players = players;
		this.alivePlayers = new ArrayList<>(players);
		this.close();
		this.getHall(this.state = 0).spawnEntities();
	}
	
	public void toNextState() {
		if(!this.hasBegon()) throw new IllegalStateException("The dungeon is not started yet");
		this.getHall(this.state).getDoor().openNext(true, this.getHall(this.state).getDoor().getTimeOpen());
		if(++this.state < this.getGameStates()) {
			this.getHall(this.state).spawnEntities();
		} else {
			String msg = "§aBravo, vous êtes parvenu à la fin du donjon ! §6Vous l'avez fait en §c"+this.getTime()+"§6. "
					+ "\n§aUne récompense vous attend...";
			for(Player p : this.alivePlayers) {
				p.sendMessage(msg);
			}
			this.prepareChest();
		}
		// TODO Next... ?
	}
	
	public void playerKilled(Player p) {
		if(!this.hasBegon()) throw new IllegalStateException("The dungeon is not started");
		if(!this.alivePlayers.contains(p)) throw new IllegalArgumentException("The player "+p.getName()+" is not an alive player of the dungeon");
		this.alivePlayers.remove(p);
		if(p.isOnline())
			p.sendMessage("§6Vous avez tenu §c"+this.getTime()+"§6.");
		if(this.alivePlayers.isEmpty()) {
			this.stop();
		}
	}
	
	public void stop() {
		if(!this.hasBegon()) throw new IllegalStateException("The dungeon is not started");
		if(this.state < this.getGameStates())
			this.getHall(this.state).killEntities();
		this.close();
		this.chest.setLock("Chest key of ("+this.name+")");
		this.state = -1;
		for(Player p : this.alivePlayers)
			p.teleport(p.getLocation().getWorld().getSpawnLocation());
		this.players = null;
		this.alivePlayers = null;
	}
	
	public void close() {
		for(Hall hall : this.halls)
			hall.getDoor().openNext(false);
	}
	
	
	
	
	
	
	
	
	@Override
	public String toString() {
		return "[Dungeon(name="+name+", minX="+minX+", maxX="+maxX+", minZ="+minZ+", maxZ="+maxZ+")]";
	}
	
	private void prepareChest() {
		Random random = new Random();
		Inventory chest = this.chest.getInventory();
		chest.clear();
		System.out.println("Prepare chest");
		for(int i = 0; i < Math.min(this.rewards.size(), chest.getSize()); i++) {
			Reward r = this.rewards.get(i);
			if(random.nextDouble() > r.prob)
				continue;
			int index;
			ItemStack is;
			while(( is = chest.getItem(index = random.nextInt(chest.getSize())) ) != null && is.getType()!=Material.AIR);
			chest.setItem(index, r.is);
			System.out.println("Set dungeon loot chest at index "+index+" "+chest.getItem(index)+" ("+r.prob+")");
		}
		this.chest.setLock(null);
	}
	
	private String getTime() {
		if(this.start == -1) throw new IllegalStateException("The time of the starting is not registred");
		double milliSeconds = System.currentTimeMillis() - this.start;
		int seconds = (int) Math.round(milliSeconds / 1000.0);
		int minuts = seconds / 60;
		seconds %= 60;
		return minuts+"'"+seconds+"''";
	}
	
}
