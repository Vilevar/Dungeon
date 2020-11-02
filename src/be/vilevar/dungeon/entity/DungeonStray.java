package be.vilevar.dungeon.entity;

import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import be.vilevar.dungeon.Hall;
import net.minecraft.server.v1_16_R1.DamageSource;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityLiving;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.EntitySkeletonStray;
import net.minecraft.server.v1_16_R1.World;

public class DungeonStray extends EntitySkeletonStray implements IDungeonEntity {

	protected Hall hall;
	
	public DungeonStray(DungeonEntityTypes<EntitySkeletonStray, ? extends DungeonStray> var0, World var1) {
		super(var0.model, var1);
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
		if(this.hall.hasBegon() && this.hall.getEntities().contains(this.getBukkitEntity()))
			if(this.hall.removeEntity(this.getBukkitEntity()) && this.hall.getDungeon().hasBegon())
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

}
