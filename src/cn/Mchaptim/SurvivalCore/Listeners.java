package cn.Mchaptim.SurvivalCore;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.events.CMIPlayerNickNameChangeEvent;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.event.ResidenceCreationEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import cn.Mchaptim.Arena.ArenaAPI;
import cn.Mchaptim.Utils.MessageControl;
import cn.Mchaptim.Utils.Bukkit.EPPlayer;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class Listeners implements Listener {

	@EventHandler
	void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerInventory inv = p.getInventory();
		if (inv.contains(new ItemStack(Material.BARRIER))) {
			ItemStack item = inv.getItem(inv.first(Material.BARRIER));
			int amount = item.getAmount();
			while (item.getAmount() != 0) {
				inv.remove(item);
				if (!inv.contains(new ItemStack(Material.BARRIER))) {
					System.out.println(p.getName() + "身上有" + amount + "屏障");
					break;
				}
				item = inv.getItem(inv.first(Material.BARRIER));
				amount += item.getAmount();
			}
		}
		if (!p.hasPermission("Mchaptim.supporter")) {
			if (CMIUser.getUser(p).getNickName() != null) {
				CMIUser.getUser(p).setNickName(null, false);
			}
			if (CMIUser.getUser(p).getTfly() == 0 && p.getAllowFlight()) {
				p.setAllowFlight(false);
				p.setFlying(false);
			}
		}
	}

	@EventHandler
	void onDeath(PlayerDeathEvent e) {
		e.deathMessage(null);
		e.setKeepInventory(true);
		e.getDrops().clear();
		e.setKeepLevel(true);
		e.setShouldDropExperience(false);
	}

	@EventHandler
	void onInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		ItemStack item = e.getItem();
		if (item == null)
			return;
		Player p = e.getPlayer();
		if (item.getType() == Material.CLOCK) {
			ConsoleCommandSender css = Bukkit.getConsoleSender();
			Bukkit.dispatchCommand(css, Config.MainMenu.replaceAll("%p", p.getName()));
			return;
		}
	}

	@EventHandler
	void onSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		Location loc = p.getEyeLocation();
		if (loc.getBlock().getType() == Material.NETHER_PORTAL && p.getLocation().getPitch() == -90)
			p.chat("/spawn");
	}

	@EventHandler
	void onPortal(PlayerPortalEvent e) {
		Player p = e.getPlayer();
		EPPlayer epp = new EPPlayer(p);
		TeleportCause tc = e.getCause();
		if (tc == TeleportCause.NETHER_PORTAL) {
			Iterator<String> tmp = Config.NetherMessage.iterator();
			while (tmp.hasNext())
				epp.sendMessage(tmp.next());
		}
	}

	@EventHandler
	void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location to = e.getTo();
		if (to.getWorld().getName().equals("Spawn") && to.getY() < 0) {
			p.chat("/spawn");
			return;
		}
		if (to.getWorld().getName().startsWith("Survival")) {
			if (Math.abs(to.getBlockX()) > 10000 | Math.abs(to.getBlockZ()) > 10000) {
				p.chat("/spawn");
				return;
			}
		}
	}

	@EventHandler
	void onNick(CMIPlayerNickNameChangeEvent e) {
		String Nick = e.getNickName().toLowerCase();
		if (Config.contain(Nick)) {
			e.setCancelled(true);
			new EPPlayer(e.getUser().getPlayer()).sendMessage("&bMchaptim &8> &f禁止使用此用户名");
			return;
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	void onResidencecreate(ResidenceCreationEvent e) {
		Player p = e.getPlayer();
		EPPlayer epp = new EPPlayer(p);
		if (e.getResidence().getWorldName().startsWith("world")) {
			e.setCancelled(true);
			epp.sendMessage("&bMchaptim &8> &f资源世界禁止创建领地");
		}
	}

	@EventHandler
	void onForm(BlockFormEvent e) {
		Block b = e.getBlock();
		if (b.getType() != Material.STONE && b.getType() != Material.COBBLESTONE) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	void onExplose(EntityExplodeEvent e) {
		switch (e.getEntityType()) {
		case CREEPER:
			e.blockList().clear();
			return;
		case MINECART_TNT:
		case ENDER_CRYSTAL:
			Location loc = e.getLocation();
			if (ResidenceApi.getResidenceManager().getByLoc(loc) != null)
				return;
			e.setCancelled(true);
			return;
		default:
			break;
		}
	}

	@EventHandler
	void onSpawn(EntitySpawnEvent e) {
		ClaimedResidence res = ResidenceApi.getResidenceManager().getByLoc(e.getLocation());
		if (res != null)
			return;
		Iterator<? extends Player> tmp = Bukkit.getOnlinePlayers().iterator();
		switch (e.getEntityType()) {
		case WITHER:
			e.getEntity().remove();
			while (tmp.hasNext()) {
				Player tmp2 = tmp.next();
				EPPlayer epp = new EPPlayer(tmp2);
				if (!tmp2.getLocation().getWorld().equals(e.getLocation().getWorld()))
					continue;
				if (tmp2.getLocation().distance(e.getLocation()) > 5)
					continue;
				epp.sendMessage("&bMchaptim &8> &f不能在领地外放置凋零");
			}
			break;
		case PRIMED_TNT:
			e.getEntity().remove();
			while (tmp.hasNext()) {
				Player tmp2 = tmp.next();
				EPPlayer epp = new EPPlayer(tmp2);
				if (!tmp2.getLocation().getWorld().equals(e.getLocation().getWorld()))
					continue;
				if (tmp2.getLocation().distance(e.getLocation()) > 5)
					continue;
				epp.sendMessage("&bMchaptim &8> &f不能在领地外点燃TNT");
			}
		default:
			return;
		}
	}

	@EventHandler
	void onMove(EntityMoveEvent e) {
		if (e.getEntityType() != EntityType.WITHER)
			return;
		if (!e.getTo().getWorld().getName().startsWith("Survival"))
			return;
		ClaimedResidence fromres = ResidenceApi.getResidenceManager().getByLoc(e.getFrom());
		ClaimedResidence tores = ResidenceApi.getResidenceManager().getByLoc(e.getTo());
		if (fromres == null) {
			e.getEntity().remove();
			return;
		}
		if (tores != null)
			return;
		e.getEntity().remove();
	}

	@EventHandler
	void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("sc.admin"))
			return;
		EPPlayer epp = new EPPlayer(p);
		Iterator<Component> sign = e.lines().iterator();
		StringBuilder sb = new StringBuilder();
		while (sign.hasNext())
			sb.append(PlainTextComponentSerializer.plainText().serialize(sign.next()));
		String str = sb.toString();
		str = str.replaceAll(" ", "");
		if (!MessageControl.hasBlacklistWord(str))
			return;
		e.setCancelled(true);
		epp.sendMessage("&bMchaptim &8> &f您编辑的信息中包含违规内容 如果您认为这是误报 请联系工作人员");
	}

	@EventHandler
	void onPrepareAnvil(PrepareAnvilEvent e) {
		ItemStack item = e.getResult();
		if (item == null)
			return;
		if (!MessageControl.hasBlacklistWord(item.getDisplayName()))
			return;
		item.setDisplayName("&f内容存在违规 如果您认为这是误报 请联系工作人员");
		e.setResult(item);
	}

	@EventHandler
	void onTeleport(PlayerTeleportEvent e) {
		if (!e.getTo().getWorld().getName().equals("Arena"))
			return;
		if (ArenaAPI.isInArena(e.getTo()))
			return;
		e.setCancelled(true);
		e.getPlayer().chat("/spawn");
	}

}
