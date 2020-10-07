package be.vilevar.dungeon.deserializer;

import java.lang.reflect.Type;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class LocationDeserializer implements DungeonJsonDeserializer<Location> {

	@Override
	public Location deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		return new Location(Bukkit.getWorld(obj.get("world").getAsString()),
				obj.get("x").getAsDouble(), obj.get("y").getAsDouble(), obj.get("z").getAsDouble());
	}

	@Override
	public Class<Location> getObjectClass() {
		return Location.class;
	}

}
