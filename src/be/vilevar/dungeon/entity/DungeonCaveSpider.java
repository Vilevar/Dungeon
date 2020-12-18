package be.vilevar.dungeon.entity;

import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import be.vilevar.dungeon.Hall;
import net.minecraft.server.v1_16_R3.DamageSource;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityCaveSpider;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.World;

public class DungeonCaveSpider extends EntityCaveSpider implements IDungeonEntity {

	protected Hall hall;
	
	public DungeonCaveSpider(DungeonEntityTypes<EntityCaveSpider, ? extends DungeonCaveSpider> entitytypes, World world) {
		super(entitytypes.model, world);
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
