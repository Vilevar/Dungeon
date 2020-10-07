package be.vilevar.dungeon;

import be.vilevar.dungeon.deserializer.DungeonDeserializerManager;

public interface IDungeonPlugin {

	public void registerPluginDeserializers(DungeonDeserializerManager manager);
}
