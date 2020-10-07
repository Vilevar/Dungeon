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
import net.minecraft.server.v1_16_R1.EntityInsentient;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.IRegistry;
import net.minecraft.server.v1_16_R1.MinecraftKey;

public class DungeonEntityTypes<T extends EntityInsentient & IDungeonEntity> extends EntityTypes<T> {

	protected static final Logger LOGGER = LogManager.getLogger();
	
	public static final DungeonEntityTypes<DungeonBlaze> DUNGEON_BLAZE = register("dungeon_blaze", EntityTypes.BLAZE, DungeonBlaze::new);
	public static final DungeonEntityTypes<DungeonCaveSpider> DUNGEON_CAVE_SPIDER = register("dungeon_cave_spider", EntityTypes.CAVE_SPIDER,
			DungeonCaveSpider::new);
	public static final DungeonEntityTypes<DungeonCreeper> DUNGEON_CREEPER = register("dungeon_creeper", EntityTypes.CREEPER,
			DungeonCreeper::new);
	public static final DungeonEntityTypes<DungeonDrowned> DUNGEON_DROWNED = register("dungeon_drowned", EntityTypes.DROWNED,
			DungeonDrowned::new);
	public static final DungeonEntityTypes<DungeonElderGuardian> DUNGEON_ELDER_GUARDIAN = register("dungeon_elder_guardian", 
			EntityTypes.ELDER_GUARDIAN, DungeonElderGuardian::new);
	public static final DungeonEntityTypes<DungeonEnderDragon> DUNGEON_ENDER_DRAGON = register("dungeon_ender_dragon",
			EntityTypes.ENDER_DRAGON, DungeonEnderDragon::new);
	public static final DungeonEntityTypes<DungeonEnderman> DUNGEON_ENDERMAN = register("dungeon_enderman", EntityTypes.ENDERMAN,
			DungeonEnderman::new);
	public static final DungeonEntityTypes<DungeonEndermite> DUNGEON_ENDERMITE = register("dungeon_endermite", EntityTypes.ENDERMITE,
			DungeonEndermite::new);
	public static final DungeonEntityTypes<DungeonEvoker> DUNGEON_EVOKER = register("dungeon_evoker", EntityTypes.EVOKER, DungeonEvoker::new);
	public static final DungeonEntityTypes<DungeonGhast> DUNGEON_GHAST = register("dungeon_ghast", EntityTypes.GHAST, DungeonGhast::new);
	public static final DungeonEntityTypes<DungeonGiant> DUNGEON_GIANT = register("dungeon_giant", EntityTypes.GIANT, DungeonGiant::new);
	public static final DungeonEntityTypes<DungeonGuardian> DUNGEON_GUARDIAN = register("dungeon_guardian", EntityTypes.GUARDIAN,
			DungeonGuardian::new);
	public static final DungeonEntityTypes<DungeonHoglin> DUNGEON_HOGLIN = register("dungeon_hoglin", EntityTypes.HOGLIN, DungeonHoglin::new);
	public static final DungeonEntityTypes<DungeonHusk> DUNGEON_HUSK = register("dungeon_husk", EntityTypes.HUSK, DungeonHusk::new);
	public static final DungeonEntityTypes<DungeonIllusioner> DUNGEON_ILLUSIONER = register("dungeon_illusioner", EntityTypes.ILLUSIONER,
			DungeonIllusioner::new);
	public static final DungeonEntityTypes<DungeonMagmaCube> DUNGEON_MAGMA_CUBE = register("dungeon_magma_cube", EntityTypes.MAGMA_CUBE,
			DungeonMagmaCube::new);
	public static final DungeonEntityTypes<DungeonPhantom> DUNGEON_PHANTOM = register("dungeon_phantom", EntityTypes.PHANTOM,
			DungeonPhantom::new);
	public static final DungeonEntityTypes<DungeonPiglin> DUNGEON_PIGLIN = register("dungeon_piglin", EntityTypes.PIGLIN, DungeonPiglin::new);
	public static final DungeonEntityTypes<DungeonPillager> DUNGEON_PILLAGER = register("dungeon_pillager", EntityTypes.PILLAGER,
			DungeonPillager::new);
	public static final DungeonEntityTypes<DungeonRavager> DUNGEON_RAVAGER = register("dungeon_ravager", EntityTypes.RAVAGER,
			DungeonRavager::new);
	public static final DungeonEntityTypes<DungeonSilverfish> DUNGEON_SILVERFISH = register("dungeon_silverfish", EntityTypes.SILVERFISH,
			DungeonSilverfish::new);
	public static final DungeonEntityTypes<DungeonSkeleton> DUNGEON_SKELETON = register("dungeon_skeleton", EntityTypes.SKELETON,
			DungeonSkeleton::new);
	public static final DungeonEntityTypes<DungeonSlime> DUNGEON_SLIME = register("dungeon_slime", EntityTypes.SLIME, DungeonSlime::new);
	public static final DungeonEntityTypes<DungeonSpider> DUNGEON_SPIDER = register("dungeon_spider", EntityTypes.SPIDER, DungeonSpider::new);
	public static final DungeonEntityTypes<DungeonStray> DUNGEON_STRAY = register("dungeon_stray", EntityTypes.STRAY, DungeonStray::new);
	public static final DungeonEntityTypes<DungeonVex> DUNGEON_VEX = register("dungeon_vex", EntityTypes.VEX, DungeonVex::new);
	public static final DungeonEntityTypes<DungeonVindicator> DUNGEON_VINDICATOR = register("dungeon_vindicator", EntityTypes.VINDICATOR,
			DungeonVindicator::new);
	public static final DungeonEntityTypes<DungeonWitch> DUNGEON_WITCH = register("dungeon_witch", EntityTypes.WITCH, DungeonWitch::new);
	public static final DungeonEntityTypes<DungeonWither> DUNGEON_WITHER = register("dungeon_wither", EntityTypes.WITHER, DungeonWither::new);
	public static final DungeonEntityTypes<DungeonWitherSkeleton> DUNGEON_WITHER_SKELETON = register("dungeon_wither_skeleton", 
			EntityTypes.WITHER_SKELETON, DungeonWitherSkeleton::new);
	public static final DungeonEntityTypes<DungeonZoglin> DUNGEON_ZOGLIN = register("dungeon_zoglin", EntityTypes.ZOGLIN, DungeonZoglin::new);
	public static final DungeonEntityTypes<DungeonZombie> DUNGEON_ZOMBIE = register("dungeon_zombie", EntityTypes.ZOMBIE, DungeonZombie::new);
	public static final DungeonEntityTypes<DungeonZombifiedPiglin> DUNGEON_ZOMBIFIED_PIGLIN = register("dungeon_zombied_piglin", 
			EntityTypes.ZOMBIFIED_PIGLIN, DungeonZombifiedPiglin::new);
	public static final DungeonEntityTypes<DungeonZombieVillager> DUNGEON_ZOMBIE_VILLAGER = register("dungeon_zombie_villager",
			EntityTypes.ZOMBIE_VILLAGER, DungeonZombieVillager::new);

	/**
	 * Do nothing, only to generate the class and register the entity types
	 */
	public static void register() {
	}
	
	public static <T extends EntityInsentient & IDungeonEntity> DungeonEntityTypes<T> register(String name, EntityTypes<? super T> model,
			EntityTypes.b<T> creator) {
		return (DungeonEntityTypes<T>) IRegistry.a(IRegistry.ENTITY_TYPE, name, new DungeonEntityTypes<>(creator, model, name));
	}
	
	
	protected final EntityTypes<? super T> model;
	
	public DungeonEntityTypes(net.minecraft.server.v1_16_R1.EntityTypes.b<T> entitytypes_b, EntityTypes<? super T> model, String name) {
		super(entitytypes_b, model.e(), model.a(), model.b(), model.c(), model.d(), ImmutableSet.of(), model.l(), model.getChunkRange(),
				model.getUpdateInterval());
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
		Entity ent = this.a(w.getHandle());
		ent.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		ent.setHeadRotation(loc.getYaw());
		((IDungeonEntity) ent).setHall(hall);
		return w.addEntity(ent, SpawnReason.CUSTOM, null);
	}
}
