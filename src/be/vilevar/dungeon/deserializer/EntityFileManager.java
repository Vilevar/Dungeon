package be.vilevar.dungeon.deserializer;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import be.vilevar.dungeon.Hall;
import be.vilevar.dungeon.entity.DungeonEntityTypes;
import net.minecraft.server.v1_16_R1.EntityTypes;

public class EntityFileManager {

	protected EntityTypes<?> type;
	protected String customName;
	protected double health;
	protected PotionEffect[] effects;
	protected ItemStack helmet;
	protected ItemStack chestplate;
	protected ItemStack leggings;
	protected ItemStack boots;
	protected ItemStack mainHand;
	protected ItemStack offHand;
	protected float helmetDropChance;
	protected float chestplateDropChance;
	protected float leggingsDropChance;
	protected float bootsDropChance;
	protected float mainHandDropChance;
	protected float offHandDropChance;
	
	public EntityFileManager(EntityTypes<?> type, String customName, double health, PotionEffect[] effects,
			ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack mainHand,
			ItemStack offHand, float helmetDropChance, float chestplateDropChance, float leggingsDropChance,
			float bootsDropChance, float mainHandDropChance, float offHandDropChance) {
		this.type = type;
		this.customName = customName;
		this.health = health;
		this.effects = effects;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		this.mainHand = mainHand;
		this.offHand = offHand;
		this.helmetDropChance = helmetDropChance;
		this.chestplateDropChance = chestplateDropChance;
		this.leggingsDropChance = leggingsDropChance;
		this.bootsDropChance = bootsDropChance;
		this.mainHandDropChance = mainHandDropChance;
		this.offHandDropChance = offHandDropChance;
	}

	@SuppressWarnings("deprecation")
	public Entity spawn(Hall hall, Location loc) {
		Entity ent;
		if(this.type instanceof DungeonEntityTypes) {
			ent = ((DungeonEntityTypes<?, ?>) type).spawn(hall, loc);
		} else {
			CraftWorld w = (CraftWorld) loc.getWorld();
			net.minecraft.server.v1_16_R1.Entity e = this.type.a(w.getHandle());
			e.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
			e.setHeadRotation(loc.getYaw());
			ent = w.addEntity(e, SpawnReason.CUSTOM, null);
		}
		if(this.customName != null) {
			ent.setCustomName(this.customName);
			ent.setCustomNameVisible(true);
		}
		if(ent instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) ent;
			if(this.health > 0) {
				le.setMaxHealth(this.health);
				le.setHealth(this.health);
			}
			if(this.effects != null && this.effects.length > 0) {
				le.addPotionEffects(Arrays.asList(this.effects));
			}
			EntityEquipment inv = le.getEquipment();
			inv.setHelmet(this.helmet);
			inv.setChestplate(this.chestplate);
			inv.setLeggings(this.leggings);
			inv.setBoots(this.boots);
			inv.setItemInMainHand(this.mainHand);
			inv.setItemInOffHand(this.offHand);
			if(this.helmetDropChance != -1)
				inv.setHelmetDropChance(this.helmetDropChance);
			if(this.chestplateDropChance != -1)
				inv.setChestplateDropChance(this.chestplateDropChance);
			if(this.leggingsDropChance != -1)
				inv.setLeggingsDropChance(this.leggingsDropChance);
			if(this.bootsDropChance != -1)
				inv.setBootsDropChance(this.bootsDropChance);
			if(this.mainHandDropChance != -1)
				inv.setItemInMainHandDropChance(this.mainHandDropChance);
			if(this.offHandDropChance != -1) 
				inv.setItemInOffHandDropChance(this.offHandDropChance);
		}
		return ent;
	}

}
