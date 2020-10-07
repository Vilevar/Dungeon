package be.vilevar.dungeon.entity;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftGiant;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import be.vilevar.dungeon.Hall;
import net.minecraft.server.v1_16_R1.DamageSource;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityGiantZombie;
import net.minecraft.server.v1_16_R1.EntityLiving;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;

public class DungeonGiant extends EntityGiantZombie implements IDungeonEntity {

	protected CraftEntity bukkitEntity;
	protected Hall hall;
	
	public DungeonGiant(EntityTypes<? extends DungeonGiant> var0, World var1) {
		super(var0, var1);
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (this.bukkitEntity == null) {
			this.setBukkitEntity(new CraftGiant(this.world.getServer(), this));
		}
		return this.bukkitEntity;
	}

	@Override
	public EntityTypes<?> getEntityType() {
		return super.getEntityType() instanceof DungeonEntityTypes ? ((DungeonEntityTypes<?>) super.getEntityType()).model : super.getEntityType();
	}

	@Override
	public boolean setGoalTarget(EntityLiving entityliving, TargetReason reason, boolean fireEvent) {
		return this.canTarget(entityliving) && super.setGoalTarget(entityliving, reason, fireEvent);
	}
	
	@Override
	public boolean isPersistent() {
		return true;
	}
	
	@Override
	public void die() {
		super.die();
		this.removeFromHall();
	}
	
	@Override
	public void die(DamageSource damagesource) {
		super.die(damagesource);
		this.removeFromHall();
	}
	
	private void removeFromHall() {
		if(this.hall.hasBegon() && this.hall.getEntities().contains(this.bukkitEntity))
			if(this.hall.removeEntity(this.bukkitEntity) && this.hall.getDungeon().hasBegon())
				this.hall.getDungeon().toNextState();
	}
	
	@Override
	public boolean isInvulnerable(DamageSource src) {
		Entity ent = src.getEntity();
		if(ent != null && ent instanceof EntityPlayer && this.hall != null && this.hall.hasBegon() && !super.isInvulnerable(src)) {
			return !this.hall.getDungeon().getAlivePlayers().contains(((EntityPlayer) ent).getBukkitEntity());
		}
		return true;
	}
	
	@Override
	public void setHall(Hall hall) {
		this.hall = hall;
	}

	@Override
	public Hall getHall() {
		return this.hall;
	}

	private boolean canTarget(EntityLiving ent) {
		if(ent instanceof EntityPlayer && this.hall != null && this.hall.hasBegon()) {
			return this.hall.getDungeon().getAlivePlayers().contains(((EntityPlayer) ent).getBukkitEntity());
		}
		return false;
	}
	
	private void setBukkitEntity(CraftEntity e) {
		this.bukkitEntity = e;
		try {
			Field field = Entity.class.getDeclaredField("bukkitEntity");
			field.setAccessible(true);
			field.set(this, e);
			field.setAccessible(false);
		} catch (Exception ex) {
			System.out.println("Couldn't modify the super bukkitEntity.");
		}
	}

}