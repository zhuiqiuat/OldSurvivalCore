package cn.Mchaptim.SurvivalCore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	static Main Plugin;

	@Override
	public void onEnable() {
		Plugin = this;
		Config.loadConfig();
		getCommand("surcore").setExecutor(new Commands());
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
//		Bukkit.getPluginManager().registerEvents(new Players(), this);
//		Bukkit.getPluginManager().registerEvents(new Entitys(), this);
//		Bukkit.getPluginManager().registerEvents(new Blocks(), this);
//		Bukkit.getPluginManager().registerEvents(new SpawnerLimit(), this);
	}

	public static Main getPlugin() {
		return Plugin;
	}

//	public static PlayerData getCaches(Player p) {
//		if (!Caches.containsKey(p)) {
//			PlayerData pd = DataAPI.loadAllData(p);
//			Main.Caches.put(p, pd);
//			return pd;
//		}
//		return Main.Caches.get(p);
//	}
//
//	public static PlayerData getCaches(String n) {
//		Player p = Bukkit.getPlayer(n);
//		if (p == null) {
//			return null;
//		}
//		return getCaches(p);
//	}
//
//	public static void removeCaches(Player p) {
//		if (!Caches.containsKey(p)) {
//			return;
//		}
//		Caches.remove(p);
//	}

//	@SuppressWarnings("deprecation")
//	public static boolean joinArena(Player p) {
//		PlayerData pd = getCaches(p);
//		if (pd.getArenaMode() == ArenaMode.FIGHTING || pd.getArenaMode() == ArenaMode.SPECING) {
//			return false;
//		}
//		double Cost = Config.ArenaJoinCost;
//		if (p.getHealth() != p.getMaxHealth()) {
//			Cost = Cost + Config.ArenaHealCost;
//			if (Main.getEconomy().getBalance(p) < Cost) {
//				p.sendMessage(Color.To("&bMchaptim &8> &7您至少需要 &f" + Cost + " 金币 &7来付入场费"));
//				return false;
//			}
//			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "heal " + p.getName());
//		} else {
//			if (Main.getEconomy().getBalance(p) < Cost) {
//				p.sendMessage(Color.To("&bMchaptim &8> &7您至少需要 &f" + Cost + " 金币 &7来付入场费"));
//				return false;
//			}
//		}
//		pd.setArenaMode(ArenaMode.FIGHTING);
//		Main.getEconomy().withdrawPlayer(p, Cost);
//		p.teleport(Config.ArenaSpawns.get(new Random().nextInt(Config.ArenaSpawns.size())));
//		p.setAllowFlight(false);
//		p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
//		p.sendMessage(Color.To("&bMchaptim &8> &7已扣除 &f" + Cost + " 金币 &7作为入场费"));
//		p.sendMessage(Color.To("&bMchaptim &8> &7正在将您传送到随机出生点"));
//		return true;
//	}
//
//	public static void leaveArena(Player p) {
//		PlayerData pd = getCaches(p);
//		if (pd.getArenaMode() != ArenaMode.FIGHTING) {
//			return;
//		}
//		if (getCaches(p).getLastPVPTime() == 0) {
//			EcoAPI.depositPlayer(p, Config.ArenaJoinCost);
//			p.sendMessage(Color.To("&bMchaptim &8> &7您没有进行战斗 已退您 &f" + Config.ArenaJoinCost + "金币 &7入场费"));
//		}
//		pd.setArenaMode(ArenaMode.NORMAL);
//	}

//	public static boolean isInArena(Player p) {
//		return getCaches(p).getArenaMode() == ArenaMode.FIGHTING;
//	}

//	public static void toggleSpec(Player p) {
//		PlayerData pd = getCaches(p);
//		if (pd == null) {
//			return;
//		}
//		if (pd.getArenaMode() == ArenaMode.FIGHTING) {
//			return;
//		}
//		if (!p.getWorld().getName().equals(Config.ArenaWorld)) {
//			p.sendMessage(Color.To("&bMchaptim &8> &7您不在竞技场 无法开启旁观者模式"));
//			return;
//		}
//		if (pd.getArenaMode() == ArenaMode.SPECING) {
//			Iterator<? extends Player> tmp = Bukkit.getOnlinePlayers().iterator();
//			while (tmp.hasNext()) {
//				tmp.next().showPlayer(Main.getPlugin(), p);
//			}
//			pd.setArenaMode(ArenaMode.NORMAL);
//			p.setAllowFlight(false);
//			p.teleport(Config.ArenaLobby);
//			p.sendMessage(Color.To("&bMchaptim &8> &7您已退出旁观者模式"));
//			return;
//		}
//		if (pd.getArenaMode() != ArenaMode.SPECING) {
//			Iterator<? extends Player> tmp = Bukkit.getOnlinePlayers().iterator();
//			while (tmp.hasNext()) {
//				tmp.next().hidePlayer(Main.Plugin, p);
//			}
//			pd.setArenaMode(ArenaMode.SPECING);
//			p.setAllowFlight(true);
//			p.setFlying(true);
//			p.setFlySpeed(0.1f);
//			p.teleport(Config.ArenaSpecSpawn);
//			p.sendMessage(Color.To("&bMchaptim &8> &7您已进入旁观者模式 输入 &f/surcore spec &7退出"));
//			return;
//		}
//	}
}
