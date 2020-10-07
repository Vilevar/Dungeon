package be.vilevar.dungeon.deserializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import be.vilevar.dungeon.Dungeon;
import be.vilevar.dungeon.DungeonPlugin;
import be.vilevar.dungeon.IDungeonPlugin;

public class DungeonDeserializerManager {

	private final DungeonPlugin pl;
	private GsonBuilder builder;
	private HashMap<String, EntityFileManager> entities = new HashMap<>();
	private HashMap<String, SpawnerFileManager> spawners = new HashMap<>();
	
	
	public DungeonDeserializerManager(DungeonPlugin pl) {
		this.pl = pl;
		this.builder = new GsonBuilder();
		this.registerDefaultDeserializers();
	}
	
	public EntityFileManager getEntityFileManager(String name) {
		return this.entities.get(name);
	}
	
	public SpawnerFileManager getSpawnerFileManager(String name) {
		return this.spawners.get(name);
	}
	
	public DungeonDeserializerManager registerDeserializer(DungeonJsonDeserializer<?> d) {
		this.builder.registerTypeHierarchyAdapter(d.getObjectClass(), d);
		return this;
	}
	
	public void registerDefaultDeserializers() {
		this.builder.setPrettyPrinting();
		
		LocationDeserializer locations = new LocationDeserializer();
		this.builder.registerTypeHierarchyAdapter(locations.getObjectClass(), locations);
		
		ItemStackDeserializer items = new ItemStackDeserializer();
		this.builder.registerTypeHierarchyAdapter(items.getObjectClass(), items);
		
		PotionEffectDeserializer potions = new PotionEffectDeserializer();
		this.builder.registerTypeHierarchyAdapter(potions.getObjectClass(), potions);
		
		DoorDeserializer doors = new DoorDeserializer();
		this.builder.registerTypeHierarchyAdapter(doors.getObjectClass(), doors);
		
		EntityFileDeserializer entities = new EntityFileDeserializer();
		this.builder.registerTypeHierarchyAdapter(entities.getObjectClass(), entities);
		
		SpawnerFileDeserializer spawners = new SpawnerFileDeserializer();
		this.builder.registerTypeHierarchyAdapter(spawners.getObjectClass(), spawners);
		
		SpawnerFileDeserializer.SpawnerThreadDeserializer threads = new SpawnerFileDeserializer.SpawnerThreadDeserializer();
		this.builder.registerTypeHierarchyAdapter(threads.getObjectClass(), threads);
		
		DungeonDeserializer dungeons = new DungeonDeserializer();
		this.builder.registerTypeHierarchyAdapter(dungeons.getObjectClass(), dungeons);
	}
	
	public void deserialize() throws Exception {
		File mainFolder = pl.getDataFolder();
		if(!mainFolder.exists())
			mainFolder.mkdir();
		
		// Load the DungeonJsonDeserializers of other plug-ins (and this plug-in too) 
		File pluginsFile = new File(mainFolder, "neededplugins.json");
		if(!pluginsFile.exists()) {
			pluginsFile.createNewFile();
			// Write default neededplugins.json
			pluginsFile = new File(mainFolder, "neededplugins.json");
			PrintStream pluginsStream = new PrintStream(pluginsFile);
			pluginsStream.append("[\"Dungeon\"]");
			pluginsStream.flush();
			pluginsStream.close();
			this.pl.registerPluginDeserializers(this);
		} else {
			InputStreamReader pluginsReader = new InputStreamReader(new FileInputStream(pluginsFile), "UTF-8");
			String[] plugins = new Gson().fromJson(pluginsReader, String[].class);
			pluginsReader.close();
			
			PluginManager pm = this.pl.getServer().getPluginManager();
			for(String plugin : plugins) {
				Plugin pl = pm.getPlugin(plugin);
				if(pl == null) {
					throw new JsonParseException("The plugin "+plugin+" from the file neededplugins.json is null.");
				} else if(!(pl instanceof IDungeonPlugin)) {
					throw new JsonParseException("The plugin "+plugin+" from the file neededplugins.json is not an IDungeonPlugin.");
				} else {
					((IDungeonPlugin) pl).registerPluginDeserializers(this);
				}
			}
		}
		
		Gson gson = this.builder.create();
		
		// Load entities
		File entitiesFolder = new File(mainFolder, "entities/");
		if(!entitiesFolder.exists()) {
			entitiesFolder.mkdir();
		} else {
			for(File entityFile : this.inFolder(entitiesFolder)) {
				InputStreamReader entityReader = new InputStreamReader(new FileInputStream(entityFile), "UTF-8");
				EntityFileManager entity = gson.fromJson(entityReader, EntityFileManager.class);
				this.entities.put(entityFile.getAbsolutePath().substring(entitiesFolder.getAbsolutePath().length() + 1, 
						entityFile.getAbsolutePath().length() - 5), entity);
				entityReader.close();
			}
		}
		
		// Load spawners
		File spawnersFolder = new File(mainFolder, "spawners/");
		if(!spawnersFolder.exists()) {
			spawnersFolder.mkdir();
		} else {
			for(File spawnerFile : this.inFolder(spawnersFolder)) {
				InputStreamReader spawnerReader = new InputStreamReader(new FileInputStream(spawnerFile), "UTF-8");
				SpawnerFileManager spawner = gson.fromJson(spawnerReader, SpawnerFileManager.class);
				this.spawners.put(spawnerFile.getAbsolutePath().substring(spawnersFolder.getAbsolutePath().length() + 1, 
						spawnerFile.getAbsolutePath().length() - 5), spawner);
				spawnerReader.close();
			}
		}
		
		// Load dungeons
		File dungeonsFolder = new File(mainFolder, "dungeons/");
		if(!dungeonsFolder.exists()) {
			dungeonsFolder.mkdir();
		} else {
			for(File dungeonFile : this.inFolder(dungeonsFolder)) {
				InputStreamReader dungeonReader = new InputStreamReader(new FileInputStream(dungeonFile), "UTF-8");
				Dungeon dungeon = gson.fromJson(dungeonReader, Dungeon.class);
				this.pl.registerDungeon(dungeonFile.getAbsolutePath().substring(dungeonsFolder.getAbsolutePath().length() + 1, 
						dungeonFile.getAbsolutePath().length() - 5), dungeon);
				dungeonReader.close();
			}
		}
	}
	
	private List<File> inFolder(File folder) {
		List<File> files = new ArrayList<>();
		if(!folder.isDirectory())
			throw new IllegalArgumentException("The file "+folder+" is not a folder.");
		for(File file : folder.listFiles()) {
			if(file.isDirectory()) files.addAll(this.inFolder(file));
			else files.add(file);
		}
		return files;
	}
}
