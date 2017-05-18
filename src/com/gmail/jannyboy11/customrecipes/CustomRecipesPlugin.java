package com.gmail.jannyboy11.customrecipes;

import org.bukkit.plugin.java.JavaPlugin;

public class CustomRecipesPlugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
	}
	
	public static CustomRecipesPlugin getInstance() {
		return JavaPlugin.getPlugin(CustomRecipesPlugin.class);
	}

}
