package be.vilevar.dungeon.deserializer;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.bukkit.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import be.vilevar.dungeon.DungeonPlugin;

public class SpawnerFileDeserializer implements DungeonJsonDeserializer<SpawnerFileManager> {

	@Override
	public SpawnerFileManager deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		// Getting custom SpawnerFileDeserializer
		SpawnerFileManager custom = this.deserializeAs(obj, ctx);
		if(custom != null)
			return custom;
		
		// Getting the SpawnerFileManager
		return new SpawnerFileManager(ctx.deserialize(obj.get("threads"), SpawnerFileManager.Thread[].class));
	}

	@Override
	public Class<SpawnerFileManager> getObjectClass() {
		return SpawnerFileManager.class;
	}

	
	
	
	public static class SpawnerThreadDeserializer implements DungeonJsonDeserializer<SpawnerFileManager.Thread[]> {

		@Override
		public SpawnerFileManager.Thread[] deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
			JsonArray array = json.getAsJsonArray();
			SpawnerFileManager.Thread[] spawners = new SpawnerFileManager.Thread[array.size()];
			
			for(int i = 0; i < spawners.length; i++) {
				JsonObject obj = array.get(i).getAsJsonObject();
				float probability = obj.get("probability").getAsFloat();
				Location loc = ctx.deserialize(obj.get("location"), Location.class);
				boolean register = obj.has("register") ? obj.get("register").getAsBoolean() : true;
				HashMap<EntityFileManager, Integer> entities = new HashMap<>();
				
				JsonArray entityArray = obj.getAsJsonArray("entities");
				for(JsonElement entityElement : entityArray) {
					JsonObject entityObj = entityElement.getAsJsonObject();
					String entityName = entityObj.get("entity").getAsString();
					EntityFileManager entity = DungeonPlugin.instance.getDeserializer().getEntityFileManager(entityName);
					if(entity == null)
						throw new JsonParseException("The entity "+entityName+" is not registred.");
					entities.put(entity, entityObj.has("count") ? entityObj.get("count").getAsInt() : 1);
				}
				
				spawners[i] = new SpawnerFileManager.Thread(probability, loc, entities, register);
			}
			
			return spawners;
		}

		@Override
		public Class<SpawnerFileManager.Thread[]> getObjectClass() {
			return SpawnerFileManager.Thread[].class;
		}
		
	}
}
