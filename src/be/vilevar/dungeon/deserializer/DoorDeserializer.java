package be.vilevar.dungeon.deserializer;

import java.lang.reflect.Type;

import org.bukkit.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import be.vilevar.dungeon.IDoor;

public class DoorDeserializer implements DungeonJsonDeserializer<IDoor> {

	@Override
	public IDoor deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		// Getting custom door
		IDoor custom = this.deserializeAs(obj, ctx);
		if(custom != null)
			return custom;
		
		// Getting IDoor.DefaultDoor
		return new IDoor.DefaultDoor(ctx.deserialize(obj.get("location"), Location.class));
	}

	@Override
	public Class<IDoor> getObjectClass() {
		return IDoor.class;
	}

	@Override
	public IDoor deserializeAs(JsonObject obj, JsonDeserializationContext ctx) {
		if (obj.has("deserialize-as")) {
			String clazzName = obj.get("deserializer-as").getAsString();
			if (!clazzName.equals("default") && !clazzName.equals(this.getObjectClass().getName()) && 
					!clazzName.equals(IDoor.DefaultDoor.class.getName())) {
				try {
					Class<?> clazz;
					boolean is = false;
					for (clazz = Class.forName(clazzName); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
						if (clazz == this.getObjectClass()) {
							is = true;
							break;
						}
					}
					if (!is) {
						throw new JsonParseException("The class " + clazz.getName() + " is not a "+this.getObjectClass().getSimpleName());
					} else {
						return ctx.deserialize(obj, clazz);
					}
				} catch (ClassNotFoundException e) {
					throw new JsonParseException(e);
				}
			}
		}
		return null;
	}
}
