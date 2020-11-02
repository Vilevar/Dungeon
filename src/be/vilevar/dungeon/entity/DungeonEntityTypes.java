package be.vilevar.dungeon.entity;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.google.common.collect.ImmutableSet;

import be.vilevar.dungeon.Hall;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityBlaze;
import net.minecraft.server.v1_16_R1.EntityCaveSpider;
import net.minecraft.server.v1_16_R1.EntityCreeper;
import net.minecraft.server.v1_16_R1.EntityDrowned;
import net.minecraft.server.v1_16_R1.EntityEnderDragon;
import net.minecraft.server.v1_16_R1.EntityEnderman;
import net.minecraft.server.v1_16_R1.EntityEndermite;
import net.minecraft.server.v1_16_R1.EntityEvoker;
import net.minecraft.server.v1_16_R1.EntityGhast;
import net.minecraft.server.v1_16_R1.EntityGiantZombie;
import net.minecraft.server.v1_16_R1.EntityGuardian;
import net.minecraft.server.v1_16_R1.EntityGuardianElder;
import net.minecraft.server.v1_16_R1.EntityHoglin;
import net.minecraft.server.v1_16_R1.EntityIllagerIllusioner;
import net.minecraft.server.v1_16_R1.EntityInsentient;
import net.minecraft.server.v1_16_R1.EntityMagmaCube;
import net.minecraft.server.v1_16_R1.EntityPhantom;
import net.minecraft.server.v1_16_R1.EntityPigZombie;
import net.minecraft.server.v1_16_R1.EntityPiglin;
import net.minecraft.server.v1_16_R1.EntityPillager;
import net.minecraft.server.v1_16_R1.EntityRavager;
import net.minecraft.server.v1_16_R1.EntitySilverfish;
import net.minecraft.server.v1_16_R1.EntitySkeleton;
import net.minecraft.server.v1_16_R1.EntitySkeletonStray;
import net.minecraft.server.v1_16_R1.EntitySkeletonWither;
import net.minecraft.server.v1_16_R1.EntitySlime;
import net.minecraft.server.v1_16_R1.EntitySpider;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.EntityVex;
import net.minecraft.server.v1_16_R1.EntityVindicator;
import net.minecraft.server.v1_16_R1.EntityWitch;
import net.minecraft.server.v1_16_R1.EntityWither;
import net.minecraft.server.v1_16_R1.EntityZoglin;
import net.minecraft.server.v1_16_R1.EntityZombie;
import net.minecraft.server.v1_16_R1.EntityZombieHusk;
import net.minecraft.server.v1_16_R1.EntityZombieVillager;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.IRegistry;
import net.minecraft.server.v1_16_R1.MinecraftKey;
import net.minecraft.server.v1_16_R1.World;

public class DungeonEntityTypes<A extends EntityInsentient, B extends A> extends EntityTypes<B> {

	protected static final Logger LOGGER = LogManager.getLogger();
	
	public static final DungeonEntityTypes<EntityBlaze, DungeonBlaze> DUNGEON_BLAZE = register("dungeon_blaze", EntityTypes.BLAZE,
			DungeonBlaze::new);
	public static final DungeonEntityTypes<EntityCaveSpider, DungeonCaveSpider> DUNGEON_CAVE_SPIDER = register("dungeon_cave_spider",
			EntityTypes.CAVE_SPIDER, DungeonCaveSpider::new);
	public static final DungeonEntityTypes<EntityCreeper, DungeonCreeper> DUNGEON_CREEPER = register("dungeon_creeper", EntityTypes.CREEPER,
			DungeonCreeper::new);
	public static final DungeonEntityTypes<EntityDrowned, DungeonDrowned> DUNGEON_DROWNED = register("dungeon_drowned", EntityTypes.DROWNED,
			DungeonDrowned::new);
	public static final DungeonEntityTypes<EntityGuardianElder, DungeonElderGuardian> DUNGEON_ELDER_GUARDIAN = register(
			"dungeon_elder_guardian", EntityTypes.ELDER_GUARDIAN, DungeonElderGuardian::new);
	public static final DungeonEntityTypes<EntityEnderDragon, DungeonEnderDragon> DUNGEON_ENDER_DRAGON = register("dungeon_ender_dragon",
			EntityTypes.ENDER_DRAGON, DungeonEnderDragon::new);
	public static final DungeonEntityTypes<EntityEnderman, DungeonEnderman> DUNGEON_ENDERMAN = register("dungeon_enderman",
			EntityTypes.ENDERMAN, DungeonEnderman::new);
	public static final DungeonEntityTypes<EntityEndermite, DungeonEndermite> DUNGEON_ENDERMITE = register("dungeon_endermite",
			EntityTypes.ENDERMITE, DungeonEndermite::new);
	public static final DungeonEntityTypes<EntityEvoker, DungeonEvoker> DUNGEON_EVOKER = register("dungeon_evoker", EntityTypes.EVOKER,
			DungeonEvoker::new);
	public static final DungeonEntityTypes<EntityGhast, DungeonGhast> DUNGEON_GHAST = register("dungeon_ghast", EntityTypes.GHAST,
			DungeonGhast::new);
	public static final DungeonEntityTypes<EntityGiantZombie, DungeonGiant> DUNGEON_GIANT = register("dungeon_giant", EntityTypes.GIANT,
			DungeonGiant::new);
	public static final DungeonEntityTypes<EntityGuardian, DungeonGuardian> DUNGEON_GUARDIAN = register("dungeon_guardian",
			EntityTypes.GUARDIAN, DungeonGuardian::new);
	public static final DungeonEntityTypes<EntityHoglin, DungeonHoglin> DUNGEON_HOGLIN = register("dungeon_hoglin", EntityTypes.HOGLIN,
			DungeonHoglin::new);
	public static final DungeonEntityTypes<EntityZombieHusk, DungeonHusk> DUNGEON_HUSK = register("dungeon_husk", EntityTypes.HUSK,
			DungeonHusk::new);
	public static final DungeonEntityTypes<EntityIllagerIllusioner, DungeonIllusioner> DUNGEON_ILLUSIONER = register("dungeon_illusioner",
			EntityTypes.ILLUSIONER, DungeonIllusioner::new);
	public static final DungeonEntityTypes<EntityMagmaCube, DungeonMagmaCube> DUNGEON_MAGMA_CUBE = register("dungeon_magma_cube",
			EntityTypes.MAGMA_CUBE, DungeonMagmaCube::new);
	public static final DungeonEntityTypes<EntityPhantom, DungeonPhantom> DUNGEON_PHANTOM = register("dungeon_phantom", EntityTypes.PHANTOM,
			DungeonPhantom::new);
	public static final DungeonEntityTypes<EntityPiglin, DungeonPiglin> DUNGEON_PIGLIN = register("dungeon_piglin", EntityTypes.PIGLIN,
			DungeonPiglin::new);
	public static final DungeonEntityTypes<EntityPillager, DungeonPillager> DUNGEON_PILLAGER = register("dungeon_pillager",
			EntityTypes.PILLAGER, DungeonPillager::new);
	public static final DungeonEntityTypes<EntityRavager, DungeonRavager> DUNGEON_RAVAGER = register("dungeon_ravager", EntityTypes.RAVAGER,
			DungeonRavager::new);
	public static final DungeonEntityTypes<EntitySilverfish, DungeonSilverfish> DUNGEON_SILVERFISH = register("dungeon_silverfish",
			EntityTypes.SILVERFISH, DungeonSilverfish::new);
	public static final DungeonEntityTypes<EntitySkeleton, DungeonSkeleton> DUNGEON_SKELETON = register("dungeon_skeleton",
			EntityTypes.SKELETON, DungeonSkeleton::new);
	public static final DungeonEntityTypes<EntitySlime, DungeonSlime> DUNGEON_SLIME = register("dungeon_slime", EntityTypes.SLIME,
			DungeonSlime::new);
	public static final DungeonEntityTypes<EntitySpider, DungeonSpider> DUNGEON_SPIDER = register("dungeon_spider", EntityTypes.SPIDER,
			DungeonSpider::new);
	public static final DungeonEntityTypes<EntitySkeletonStray, DungeonStray> DUNGEON_STRAY = register("dungeon_stray", EntityTypes.STRAY,
			DungeonStray::new);
	public static final DungeonEntityTypes<EntityVex, DungeonVex> DUNGEON_VEX = register("dungeon_vex", EntityTypes.VEX, DungeonVex::new);
	public static final DungeonEntityTypes<EntityVindicator, DungeonVindicator> DUNGEON_VINDICATOR = register("dungeon_vindicator",
			EntityTypes.VINDICATOR, DungeonVindicator::new);
	public static final DungeonEntityTypes<EntityWitch, DungeonWitch> DUNGEON_WITCH = register("dungeon_witch", EntityTypes.WITCH,
			DungeonWitch::new);
	public static final DungeonEntityTypes<EntityWither, DungeonWither> DUNGEON_WITHER = register("dungeon_wither", EntityTypes.WITHER,
			DungeonWither::new);
	public static final DungeonEntityTypes<EntitySkeletonWither, DungeonWitherSkeleton> DUNGEON_WITHER_SKELETON = register(
			"dungeon_wither_skeleton", EntityTypes.WITHER_SKELETON, DungeonWitherSkeleton::new);
	public static final DungeonEntityTypes<EntityZoglin, DungeonZoglin> DUNGEON_ZOGLIN = register("dungeon_zoglin", EntityTypes.ZOGLIN,
			DungeonZoglin::new);
	public static final DungeonEntityTypes<EntityZombie, DungeonZombie> DUNGEON_ZOMBIE = register("dungeon_zombie", EntityTypes.ZOMBIE,
			DungeonZombie::new);
	public static final DungeonEntityTypes<EntityPigZombie, DungeonZombifiedPiglin> DUNGEON_ZOMBIFIED_PIGLIN = register(
			"dungeon_zombied_piglin", EntityTypes.ZOMBIFIED_PIGLIN, DungeonZombifiedPiglin::new);
	public static final DungeonEntityTypes<EntityZombieVillager, DungeonZombieVillager> DUNGEON_ZOMBIE_VILLAGER = register(
			"dungeon_zombie_villager", EntityTypes.ZOMBIE_VILLAGER, DungeonZombieVillager::new);

	/**
	 * Do nothing, only to generate the class and register the entity types
	 */
	public static void register() {
	}
	
	public static <A extends EntityInsentient, B extends A> DungeonEntityTypes<A, B> register(String name, EntityTypes<A> model,
			IDungeonEntityTypeCreator<A, B> creator) {
		return (DungeonEntityTypes<A, B>) IRegistry.a(IRegistry.ENTITY_TYPE, name, new DungeonEntityTypes<>(creator, model, name));
	}
	
	
	protected final EntityTypes<A> model;
	protected final IDungeonEntityTypeCreator<A, B> creator;
	
	public DungeonEntityTypes(IDungeonEntityTypeCreator<A, B> creator, EntityTypes<A> model, String name) {
		super(null, model.e(), model.a(), model.b(), model.c(), model.d(), ImmutableSet.of(), model.l(), model.getChunkRange(),
				model.getUpdateInterval());
		this.creator = creator;
		this.model = model;
	}
	
	@Override
	public String f() {
		return this.model.f();
	}

	@Override
	public IChatBaseComponent g() {
		return this.model.g();
	}
	
	@Override
	public MinecraftKey i() {
		return this.model.i();
	}
	
	public org.bukkit.entity.Entity spawn(@Nonnull Hall hall, Location loc) {
		CraftWorld w = (CraftWorld) loc.getWorld();
		Entity ent = this.creator.create(this, w.getHandle());
		ent.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		ent.setHeadRotation(loc.getYaw());
		((IDungeonEntity) ent).setHall(hall);
		return w.addEntity(ent, SpawnReason.CUSTOM, null);
	}
	
	@FunctionalInterface
	public static interface IDungeonEntityTypeCreator<A extends EntityInsentient, B extends A> {
		B create(DungeonEntityTypes<A, B> entityTypes, World world);
	}
}
