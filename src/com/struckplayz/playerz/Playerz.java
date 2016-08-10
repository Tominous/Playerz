package com.struckplayz.playerz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Playerz extends JavaPlugin {
	
	File config = new File("plugins/Playerz/config.yml");
	FileConfiguration fc = new YamlConfiguration();
	
	public void onEnable() {
		createFiles();
	}
	
	public void onDisable() {
		
	}
	
	public void createFiles() {
		if (config.exists()) {
			return;
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
			fc.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("list")) {
			try {
				fc.load(config);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			String list = "";
			boolean display_prefix = fc.getBoolean("List.DisplayPrefix");
			boolean display_suffix = fc.getBoolean("List.DisplaySuffix");
			String prefix = color(fc.getString("List.Prefix"));
			String suffix = color(fc.getString("List.Suffix"));
			String max = color(fc.getString("List.PlayerAmount").replaceAll("%ONLINEPLAYERS%", Bukkit.getOnlinePlayers().size() + "").replaceAll("%MAXPLAYERS%", Bukkit.getMaxPlayers() + ""));
			int nog = fc.getInt("List.NumberOfGroups");
			if (display_prefix == true) {
				list = list + prefix + "\n";
			}
			list = list + max + "\n";
			for (int i = 1; i<nog + 1; i++) {
				String g = fc.getString("List." + i);
				String var = "%GROUP" + i + "%";
				ArrayList<String> a = new ArrayList<String>();
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (all.hasPermission("playerz." + i)) {
						a.add(all.getName());
					}
				}
				String f = g.replaceAll(var, deconstructList(a)).replaceAll("%online%", a.size() + "");
				if (a.size() == 0) {
					f = f + "&cnone";
				}
				list = list + f + "\n";
			}
			if (display_suffix == true) {
				list = list + suffix;
			}
			sender.sendMessage(color(list));
		}
		return true;
	}
	
	public static String deconstructList(List<String> s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i<s.size(); i++) {
			if (i + 1 == s.size()) {
				sb.append(s.get(i));
				return sb.toString();
			}
			sb.append(s.get(i) + ", ");
		}
		return sb.toString();
	}
	
	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
}
