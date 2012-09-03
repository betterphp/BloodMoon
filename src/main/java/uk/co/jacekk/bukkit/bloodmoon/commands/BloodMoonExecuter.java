package uk.co.jacekk.bukkit.bloodmoon.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.v1.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v1.command.CommandHandler;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Permission;

public class BloodMoonExecuter extends BaseCommandExecutor<BloodMoon> {
	
	public BloodMoonExecuter(BloodMoon plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"bloodmoon", "blood", "bm"}, description = "Toggles the bloodmoon for the current world.", usage = "[start/stop] [world_name]")
	public void execute(CommandSender sender, String label, String[] args){
		if (args.length != 1 && args.length != 2){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " [start/stop] [world_name]"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " start"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " stop world"));
			return;
		}
		
		if (!(sender instanceof Player) && args.length != 2){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You must specify a world when using the command from the console"));
			return;
		}
		
		String option = args[0];
		String worldName = (args.length == 2) ? args[1] : ((Player) sender).getWorld().getName();
		
		if (!plugin.config.getStringList(Config.AFFECTED_WORLDS).contains(worldName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "The blood moon is not enabled for this world"));
			return;
		}
		
		if (option.equalsIgnoreCase("start")){
			if (!Permission.ADMIN_START.hasPermission(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to start a bloodmoon"));
				return;
			}
			
			plugin.activate(worldName);
		}else if (option.equalsIgnoreCase("stop")){
			if (!Permission.ADMIN_STOP.hasPermission(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to stop a bloodmoon"));
				return;
			}
			
			plugin.deactivate(worldName);
		}else{
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Invalid option, see /" + label + " for correct usage"));
		}
	}
	
}