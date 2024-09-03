package cn.Mchaptim.SurvivalCore;

import java.util.List;

import org.bukkit.command.CommandSender;
import cn.Mchaptim.Utils.Bukkit.EPCommand;

public class Commands extends EPCommand {

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("sc.admin")) {
			Config.loadConfig();
			sendMessage(sender, "&bMchaptim &8> &f配置重载成功");
			return;
		}

	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, String[] arg1) {
		return null;
	}

}
