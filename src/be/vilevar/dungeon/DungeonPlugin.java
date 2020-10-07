package be.vilevar.dungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import be.vilevar.dungeon.deserializer.DungeonDeserializerManager;
import be.vilevar.dungeon.entity.DungeonEntityTypes;

public class DungeonPlugin extends JavaPlugin implements Listener, IDungeonPlugin {

	public static DungeonPlugin instance;

	private HashMap<String, Dungeon> dungeons = new HashMap<>();
	private DungeonDeserializerManager deserializer;
	
	@Override
	public void onLoad() {
		instance = this;
		this.deserializer = new DungeonDeserializerManager(this);
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("dungeon").setExecutor(this);
		DungeonEntityTypes.register();
		try {
			this.deserializer.deserialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		for(Dungeon d : this.getDungeons())
			if(d.hasBegon())
				d.stop();
	}
	
	public void registerDungeon(String id, Dungeon dungeon) {
		for(Dungeon d : this.getDungeons()) {
			if(this.testConcurrenceBetween(dungeon, d)) {
				throw new IllegalArgumentException("The dungeon "+id+" cannot be registred because there are concurrence of area between this and the"
						+ " dungeon "+d.getName());
			}
		}
		this.dungeons.put(id, dungeon);
		dungeon.close();
	}
	
	public Dungeon getDungeon(String name) {
		return this.dungeons.get(name);
	}
	
	public Collection<Dungeon> getDungeons() {
		return this.dungeons.values();
	}
	
	public Dungeon getDungeonAt(Location loc) {
		for(Dungeon d : this.dungeons.values())
			if(d.isInDungeon(loc))
				return d;
		return null;
	}
	
	public DungeonDeserializerManager getDeserializer() {
		return this.deserializer;
	}
	
	private String noBuild = "§cVous n'avez pas la permission de construire sur le territoire du donjon §6";
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Dungeon d = this.getDungeonAt(e.getBlock().getLocation());
		if(d != null && !e.getPlayer().hasPermission("dungeon.build")) {
			e.getPlayer().sendMessage(noBuild+d.getName()+"§c.");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Dungeon d = this.getDungeonAt(e.getBlock().getLocation());
		if(d != null && !e.getPlayer().hasPermission("dungeon.build")) {
			e.getPlayer().sendMessage(noBuild+d.getName()+"§c.");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		if(this.getDungeonAt(e.getLocation())!=null && e.getSpawnReason()!=SpawnReason.CUSTOM)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Dungeon d = this.getDungeonAt(p.getLocation());
		if(d != null && d.hasBegon() && d.getAlivePlayers().contains(p)) {
			p.teleport(p.getLocation().getWorld().getSpawnLocation());
			d.playerKilled(p);
		}
	}
	
/*	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Dungeon d = this.getDungeonAt(p.getLocation());
		if(d != null && d.hasBegon() && d.getAlivePlayers().contains(p)) {
			d.playerKilled(p);
		}
	}*/
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity ent = e.getEntity();
		Dungeon d = this.getDungeonAt(ent.getLocation());
		if(d != null && d.hasBegon() && !d.isGameFinished()) {
			if(ent instanceof Player) {
				d.playerKilled((Player) ent);
	//		} else {
	//			Hall hall = d.getHall(d.getState());
	//			if(hall.getEntities().contains(ent) && hall.removeEntity(ent)) {
	//				d.toNextState();
	//			}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		Entity ent = e.getEntity();
		Dungeon d = this.getDungeonAt(ent.getLocation());
		if(d != null && d.hasBegon()) {
			if(d.isGameFinished()) {
				e.setCancelled(true);
			} else if(ent instanceof Player) {
				List<Player> alive = d.getAlivePlayers();
				Entity damager = e.getDamager();
				if(damager instanceof Player) {
					e.setCancelled(alive.contains(ent) && alive.contains(damager));
				} else if(damager instanceof Projectile) {
					Projectile p = (Projectile) damager;
					if(p.getShooter() != null && p.getShooter() instanceof Player) {
						e.setCancelled(alive.contains(ent) && alive.contains(p.getShooter()));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Dungeon from = this.getDungeonAt(e.getFrom());
		Dungeon to = this.getDungeonAt(e.getTo());
		if(from != to && ( (from != null && from.hasBegon() && from.getAlivePlayers().contains(e.getPlayer())) || (to != null && to.hasBegon()) ) &&
				!e.getPlayer().hasPermission("dungeon.teleport")) {
			e.getPlayer().sendMessage("§cVous ne pouvez pas vous déplacer entre des zones de donjons tant qu'au moins l'un d'entre eux est activé.");
			e.setCancelled(true);
		}
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("dungeon") && sender.hasPermission("dungeon.command")) {
			if(args.length == 0) {
				if(sender.hasPermission("dungeon.command.start"))
					sender.sendMessage("§a/dungeon §c<start> [dungeon] [players]");
				if(sender.hasPermission("dungeon.command.stop"))
					sender.sendMessage("§a/dungeon §c<stop> [dungeon]");
				return true;
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("start") && sender.hasPermission("dungeon.command.start")) {
					sender.sendMessage("§a/dungeon start §c[dungeon] [players]");
				} else if(args[0].equalsIgnoreCase("stop") && sender.hasPermission("dungeon.command.stop")) {
					sender.sendMessage("§a/dungeon stop §c[dungeon]");
				} else {
					sender.sendMessage("§cUnknown command.");
					return false;
				}
				return true;
			} else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("start") && sender.hasPermission("dungeon.command.start")) {
					sender.sendMessage("§a/dungeon start "+args[1]+" §c[players]");
				} else if(args[0].equalsIgnoreCase("stop") && sender.hasPermission("dungeon.command.stop")) {
					Dungeon dungeon = this.getDungeon(args[1]);
					if(dungeon == null) {
						sender.sendMessage("§cThe dungeon §6"+args[1]+"§c does not exist.");
					} else if(!dungeon.hasBegon()) {
						sender.sendMessage("§cThe dungeon §6"+args[1]+"§c is not started.");
					} else {
						dungeon.stop();
						sender.sendMessage("§6The dungeon §c"+args[1]+"§6 is now §astopped§6.");
					}
				} else {
					sender.sendMessage("§cUnknown command.");
					return false;
				}
				return true;
			} else {
				if(args[0].equalsIgnoreCase("start") && sender.hasPermission("dungeon.command.start")) {
					Dungeon dungeon = this.getDungeon(args[1]);
					if(dungeon == null) {
						sender.sendMessage("§cThe dungeon §6"+args[1]+"§c does not exist.");
					} else if(dungeon.hasBegon()) {
						sender.sendMessage("§cThe dungeon §6"+args[1]+"§c has started yet.");
					} else {
						ArrayList<Player> players = new ArrayList<>();
						for(int i = 2; i < args.length; i++) {
							String playerName = args[i];
							Player p = Bukkit.getPlayer(playerName);
							if(p == null) {
								sender.sendMessage("§cThe player §6"+playerName+"§c is not online.");
							} else if(!dungeon.equals(this.getDungeonAt(p.getLocation()))) {
								sender.sendMessage("§cThe player §6"+playerName+"§c is not in the area of the dungeon §6"+args[1]+"§c.");
							} else if(players.contains(p)) {
								sender.sendMessage("§cDo not try to engage twice a player (§6"+playerName+")§c.");
							} else {
								players.add(p);
								sender.sendMessage("§6The player §a"+playerName+"§6 is now engaged to fight the dungeon !");
								p.sendMessage("§a"+sender.getName()+"§6 engaged you to fight the dungeon "+dungeon.getName()+"§6 !");
							}
						}
						if(!players.isEmpty())
							dungeon.start(players);
					}
					return true;
				} else {
					sender.sendMessage("§cUnknown command.");
					return false;
				}
			}
		}
		return super.onCommand(sender, command, label, args);
	}
	
	
	@Override
	public void registerPluginDeserializers(DungeonDeserializerManager manager) {
		manager.registerDefaultDeserializers();
	}
	
	
	private boolean testConcurrenceBetween(Dungeon d1, Dungeon d2) {
		World w = d1.getRewardLocation().getWorld();
		if(w.equals(d2.getRewardLocation().getWorld())) {
			Location corner00 = new Location(w, d2.getMinX(), 0, d2.getMinZ());
			Location corner01 = new Location(w, d2.getMinX(), 0, d2.getMaxZ());
			Location corner10 = new Location(w, d2.getMaxX(), 0, d2.getMinZ());
			Location corner11 = new Location(w, d2.getMaxX(), 0, d2.getMaxZ());
			if(d1.isInDungeon(corner00) || d1.isInDungeon(corner01) || d1.isInDungeon(corner10) || d1.isInDungeon(corner11)) {
				return true;
			} else {
				return this.testLargeConcurrence(d1, d2) || this.testLargeConcurrence(d2, d1);
			}
		}
		return false;
	}
	
	private boolean testLargeConcurrence(Dungeon d1, Dungeon d2) {
		// Dungeon 1
		int min1X = d1.getMinX();
		int min1Z = d1.getMinZ();
		int max1X = d1.getMaxX();
		int max1Z = d1.getMaxZ();
		// Dungeon 2
		int min2X = d2.getMinX();
		int min2Z = d2.getMinZ();
		int max2X = d2.getMaxX();
		int max2Z = d2.getMaxZ();
		// Test
		return min2X <= min1X && max1X <= max2X && min1Z <= min2Z && max2Z <= max1Z;
	}
}
