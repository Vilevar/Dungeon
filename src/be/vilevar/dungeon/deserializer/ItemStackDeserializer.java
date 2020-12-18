package be.vilevar.dungeon.deserializer;

import java.lang.reflect.Type;

import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_16_R3.IMaterial;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.MojangsonParser;

public class ItemStackDeserializer implements DungeonJsonDeserializer<ItemStack> {

	@Override
	public ItemStack deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		IMaterial item = IRegistry.ITEM.get(new MinecraftKey(obj.get("type").getAsString()));
		int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
		
		net.minecraft.server.v1_16_R3.ItemStack is = new net.minecraft.server.v1_16_R3.ItemStack(item, count);
		
		if(obj.has("tag")) {
			try {
				is.setTag(MojangsonParser.parse(obj.get("tag").getAsString()));
			} catch (CommandSyntaxException e) {
				throw new JsonParseException(e);
			}
		}
		
		return CraftItemStack.asBukkitCopy(is);
	}

	@Override
	public Class<ItemStack> getObjectClass() {
		return ItemStack.class;
	}

}
