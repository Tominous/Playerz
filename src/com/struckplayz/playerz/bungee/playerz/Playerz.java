package com.struckplayz.bungee.playerz;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Playerz extends Plugin {
	
	public static File config = new File("plugins/Playerz/config.yml");
	
	public void onEnable() {
		createFiles();
		getProxy().getPluginManager().registerCommand(this, new ListCommand());
	}
	
	public void onDisable() {
		
	}

	public void createFiles() {
		if (config.exists()) {
			return;
		}
		Configuration fc = null;
		try {
			fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
		} catch (IOException e1) {
			File f = new File(config.getPath().replaceAll("config.yml", ""));
			f.mkdirs();
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
			} catch (IOException e) {
				return;
			}
		}
		fc.set("Permissions.Enabled.List", false);
		fc.set("Permissions.Enabled.Toggle", true);
		fc.set("Permissions.OpOverride", true);
		fc.set("Permissions.Denied", "&4[Playerz] &fYou aren't allowed to use this command.");
		fc.set("List.NumberOfGroups", 1);
		fc.set("List.DisplayPrefix", true);
		fc.set("List.DisplaySuffix", true);
		fc.set("List.Prefix", "&6----------------------[&ePlayerz&6]-----------------------");
		fc.set("List.PlayerAmount", "&3There are (&6%ONLINEPLAYERS%&3/&6%MAXPLAYERS%&3) players online.");
		int nog = fc.getInt("List.NumberOfGroups");
		for (int i = 1; i<nog + 1; i++) {
			fc.set("List." + i, "&3Group " + i + ": &6%GROUP" + i + "%");
		}
		fc.set("List.Suffix", "&6-----------------------------------------------------");
		fc.set("Broadcast.RunOnStart", false);
		fc.set("Broadcast.Interbal", 300);
		fc.set("Broadcast.DisplayPrefix", true);
		fc.set("Broadcast.DisplaySuffix", true);
		fc.set("Broadcast.DisplayPlayerAmount", true);
		fc.set("Broadcast.DisplayGroups", true);
		fc.set("Broadcast.Start", "&4[Playerz] &fAutomatic list broadcasts started.");
		fc.set("Broadcast.Stop", "&4[Playerz] &fAutomatic list broadcasts stopped.");
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(fc, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
