package be.vilevar.dungeon.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface DungeonJsonDeserializer<T> extends JsonDeserializer<T> {
	
	public Class<T> getObjectClass();
	
	default T deserializeAs(JsonObject obj, JsonDeserializationContext ctx) {
		if (obj.has("deserialize-as")) {
			String clazzName = obj.get("deserializer-as").getAsString();
			if (!clazzName.equals("default") && !clazzName.equals(this.getObjectClass().getName())) {
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
