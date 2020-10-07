package be.vilevar.dungeon.deserializer;

import java.lang.reflect.Type;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PotionEffectDeserializer implements DungeonJsonDeserializer<PotionEffect[]> {

	@Override
	public PotionEffect[] deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
		JsonArray array = json.getAsJsonArray();
		PotionEffect[] effs = new PotionEffect[array.size()];
		for(int i = 0; i < array.size(); i++) {
			JsonElement sub = array.get(i);
			JsonObject obj = sub.getAsJsonObject();
			
			String typeName = obj.get("effect-type").getAsString();
			PotionEffectType type = PotionEffectType.getByName(typeName);
			if(type == null) {
				throw new JsonParseException("The effect type "+typeName+" is not registred.");
			} else {
				int duration = obj.has("duration") ? obj.get("duration").getAsInt() : 999999;
				int amplifier = obj.has("level") ? obj.get("level").getAsInt() - 1 : 0;
				boolean ambient = obj.has("ambient") ? obj.get("ambient").getAsBoolean() : true;
				boolean particles = obj.has("particles") ? obj.get("particles").getAsBoolean() : true;
				effs[i] = new PotionEffect(type, duration, amplifier, ambient, particles);
			}
		}
		return effs;
	}

	@Override
	public Class<PotionEffect[]> getObjectClass() {
		return PotionEffect[].class;
	}

}
