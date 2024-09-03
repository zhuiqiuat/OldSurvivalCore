package cn.Mchaptim.SurvivalCore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	public static String MainMenu;
	public static List<String> NetherMessage = new ArrayList<>();
	public static List<String> NickBadWord = new ArrayList<>();

	public static void loadConfig() {
		Main.getPlugin().saveDefaultConfig();
		Main.getPlugin().reloadConfig();
		FileConfiguration Config = Main.getPlugin().getConfig();
		MainMenu = Config.getString("MainMenu.Command");
		NetherMessage = Config.getStringList("Portal.Nether");
		NickBadWord = Config.getStringList("NickName.BadWord");
	}

	public static void saveConfig() {
		FileConfiguration Config = Main.getPlugin().getConfig();
		Config.set("MainMenu.Command", MainMenu);
		try {
			Config.save(new File(Main.getPlugin().getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean contain(String s) {
		Iterator<String> tmp = NickBadWord.iterator();
		while (tmp.hasNext()) {
			if (s.toLowerCase().indexOf(tmp.next().toLowerCase()) != -1) {
				return true;
			}
		}
		return false;
	}
}
