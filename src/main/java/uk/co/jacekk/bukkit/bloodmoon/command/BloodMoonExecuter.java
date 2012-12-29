package uk.co.jacekk.bukkit.bloodmoon.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.v7.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v7.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.v7.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Permission;

public class BloodMoonExecuter extends BaseCommandExecutor<BloodMoon> {
	
	public BloodMoonExecuter(BloodMoon plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"bloodmoon", "blood", "bm"}, description = "Toggles the bloodmoon for the current world.", usage = "[start/stop] [world_name]")
	@CommandTabCompletion({"start|next|stop|debug"})
	public void execute(CommandSender sender, String label, String[] args){
		if (args.length != 1 && args.length != 2){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " [action] [world_name]"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Actions:"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "  start - Forces a bloodmoon to start"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "  next - Forces a bloodmoon to start at the next night"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "  stop - Stops a bloodmoon"));
			return;
		}
		
		if (!(sender instanceof Player) && args.length != 2){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You must specify a world when using the command from the console"));
			return;
		}
		
		String option = args[0];
		String worldName = (args.length == 2) ? args[1] : ((Player) sender).getWorld().getName();
		
		if (!plugin.isEnabled(worldName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "The blood moon is not enabled for this world"));
			return;
		}
		
		if (option.equalsIgnoreCase("start")){
			if (!Permission.ADMIN_START.has(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to start a bloodmoon"));
				return;
			}
			
			plugin.activate(worldName);
			
			sender.sendMessage(ChatColor.GREEN + "Bloodmoon started in " + worldName);
		}else if (option.equalsIgnoreCase("next")){
			if (!Permission.ADMIN_START.has(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to start a bloodmoon"));
				return;
			}
			
			plugin.forceNextNight(worldName);
			
			sender.sendMessage(ChatColor.GREEN + "Bloodmoon forced in " + worldName);
		}else if (option.equalsIgnoreCase("stop")){
			if (!Permission.ADMIN_STOP.has(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to stop a bloodmoon"));
				return;
			}
			
			plugin.deactivate(worldName);
			
			sender.sendMessage(ChatColor.GREEN + "Bloodmoon stopped in " + worldName);
		}else{
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Invalid option, see /" + label + " for correct usage"));
		}
	}
	
}