package be.vilevar.dungeon.deserializer;

import java.lang.reflect.Type;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.IRegistry;
import net.minecraft.server.v1_16_R1.MinecraftKey;

public class EntityFileDeserializer implements DungeonJsonDeserializer<EntityFileManager> {

	@Override
	public EntityFileManager deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		// Getting custom EntityFileDeserialize
		EntityFileManager custom = this.deserializeAs(obj, ctx);
		if(custom != null)
			return custom;
		
		// Getting the EntityFileManager
		String id = obj.get("entity-type").getAsString();
		EntityTypes<?> type = IRegistry.ENTITY_TYPE.get(new MinecraftKey(id));
		if(type == null) {
			throw new JsonParseException("The entity type "+id+" is not registred.");
		}
		String customName = obj.has("name") ? obj.get("name").getAsString() : null;
		double health = obj.has("health") ? obj.get("health").getAsDouble() : -1;
		PotionEffect[] effects = obj.has("effects") ? ctx.deserialize(obj.get("effects"), PotionEffect[].class) : null;
		ItemStack helmet = obj.has("helmet") ? ctx.deserialize(obj.get("helmet"), ItemStack.class) : null;
		ItemStack chestplate = obj.has("chestplate") ? ctx.deserialize(obj.get("chestplate"), ItemStack.class) : null;
		ItemStack leggings = obj.has("leggings") ? ctx.deserialize(obj.get("leggings"), ItemStack.class) : null;
		ItemStack boots = obj.has("boots") ? ctx.deserialize(obj.get("boots"), ItemStack.class) : null;
		ItemStack mainHand = obj.has("main-hand") ? ctx.deserialize(obj.get("main-hand"), ItemStack.class) : null;
		ItemStack offHand = obj.has("off-hand") ? ctx.deserialize(obj.get("off-hand"), ItemStack.class) : null;
		float helmetDropChance = obj.has("helmet-drop-chance") ? obj.get("helmet-drop-chance").getAsFloat() : -1;
		float chestplateDropChance = obj.has("chestplate-drop-chance") ? obj.get("chestplate-drop-chance").getAsFloat() : -1;
		float leggingsDropChance = obj.has("leggings-drop-chance") ? obj.get("leggings-drop-chance").getAsFloat() : -1;
		float bootsDropChance = obj.has("boots-drop-chance") ? obj.get("boots-drop-chance").getAsFloat() : -1;
		float mainHandDropChance = obj.has("main-hand-drop-chance") ? obj.get("main-hand-drop-chance").getAsFloat() : -1;
		float offHandDropChance = obj.has("off-hand-drop-chance") ? obj.get("off-hand-drop-chance").getAsFloat() : -1;
		return new EntityFileManager(type, customName, health, effects, helmet, chestplate, leggings, boots, mainHand, offHand, helmetDropChance,
				chestplateDropChance, leggingsDropChance, bootsDropChance, mainHandDropChance, offHandDropChance);
	}

	@Override
	public Class<EntityFileManager> getObjectClass() {
		return EntityFileManager.class;
	}

}
