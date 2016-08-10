package com.struckplayz.bungee.playerz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ListCommand extends Command {
	
	public ListCommand() {
		super("list");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] strings) {
		Configuration fc = null;
		try {
			fc = ConfigurationProvider.getProvider(YamlConfiguration.class).load(Playerz.config);
		} catch (IOException e1) {
			
		}
		String list = "";
		boolean display_prefix = fc.getBoolean("List.DisplayPrefix");
		boolean display_suffix = fc.getBoolean("List.DisplaySuffix");
		String prefix = color(fc.getString("List.Prefix"));
		String suffix = color(fc.getString("List.Suffix"));
		String max = color(fc.getString("List.PlayerAmount").replaceAll("%ONLINEPLAYERS%", BungeeCord.getInstance().getPlayers().size() + "").replaceAll("%MAXPLAYERS%", BungeeCord.getInstance().getConfig().getPlayerLimit() + ""));
		int nog = fc.getInt("List.NumberOfGroups");
		if (display_prefix == true) {
			list = list + prefix + "\n";
		}
		list = list + max + "\n";
		for (int i = 1; i<nog + 1; i++) {
			String g = fc.getString("List." + i);
			String var = "%GROUP" + i + "%";
			ArrayList<String> a = new ArrayList<String>();
			for (ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
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
		return;
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
