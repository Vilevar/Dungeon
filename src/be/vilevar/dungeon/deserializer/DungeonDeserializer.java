package be.vilevar.dungeon.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import be.vilevar.dungeon.Dungeon;
import be.vilevar.dungeon.DungeonPlugin;
import be.vilevar.dungeon.Hall;
import be.vilevar.dungeon.IDoor;
import be.vilevar.dungeon.ISpawner;
import be.vilevar.dungeon.Reward;

public class DungeonDeserializer implements DungeonJsonDeserializer<Dungeon> {

	@Override
	public Dungeon deserialize(JsonElement json, Type srcType, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();

		// Getting custom DungonDeserializer
		Dungeon custom = this.deserializeAs(obj, ctx);
		if(custom != null)
			return custom;

		// Getting the Dungeon
		String name = obj.get("name").getAsString();
		int minX = obj.get("min-x").getAsInt();
		int maxX = obj.get("max-x").getAsInt();
		int minZ = obj.get("min-z").getAsInt();
		int maxZ = obj.get("max-z").getAsInt();
		List<Reward> rewards = new ArrayList<>();
		Location rewardLocation = ctx.deserialize(obj.get("reward-location"), Location.class);
		
		JsonArray rewardArray = obj.getAsJsonArray("rewards");
		for(JsonElement rewardElement : rewardArray) {
			JsonObject rewardObj = rewardElement.getAsJsonObject();
			rewards.add(new Reward(ctx.deserialize(rewardObj.get("item"), ItemStack.class), rewardObj.get("probability").getAsDouble()));
		}
		
		Dungeon dungeon = new Dungeon(name, minX, maxX, minZ, maxZ, rewards, rewardLocation);
		
		// Getting halls
		JsonArray hallArray = obj.getAsJsonArray("halls");
		if(hallArray.size() == 0)
			throw new JsonParseException("The halls array cannot be empty");
		Hall[] halls = new Hall[hallArray.size()];
		for(int i = 0; i < halls.length; i++) {
			JsonObject hallObj = hallArray.get(i).getAsJsonObject();
			IDoor door = ctx.deserialize(hallObj.get("door"), IDoor.class);
			
			String spawnerName = hallObj.get("spawner").getAsString();
			SpawnerFileManager spawnerFile = DungeonPlugin.instance.getDeserializer().getSpawnerFileManager(spawnerName);
			if(spawnerFile == null)
				throw new JsonParseException("The spawner "+spawnerName+" is not registred.");
			
			ISpawner spawner = spawnerFile::spawn;
			halls[i] = new Hall(i, dungeon, door, spawner);
		}
		
		dungeon.addHalls(halls);
		
		return dungeon;
	}

	@Override
	public Class<Dungeon> getObjectClass() {
		return Dungeon.class;
	}

}
