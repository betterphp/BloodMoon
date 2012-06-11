package uk.co.jacekk.bukkit.bloodmoon.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Permission;

public class BloodMoonExecuter extends BaseCommandExecutor<BloodMoon> {
	
	public BloodMoonExecuter(BloodMoon plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (args.length != 1 && args.length != 2){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Usage: /" + label + " [start/stop] [world_name]"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " start"));
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Example: /" + label + " stop world"));
			return true;
		}
		
		if (!(sender instanceof Player) && args.length != 2){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You must specify a world when using the command from the console"));
			return true;
		}
		
		String option = args[0];
		String worldName = (args.length == 2) ? args[1] : ((Player) sender).getWorld().getName();
		
		if (!plugin.config.getStringList(Config.AFFECTED_WORLDS).contains(worldName)){
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "The blood moon is not enabled for this world"));
			return true;
		}
		
		World world = plugin.server.getWorld(worldName);
		
		if (option.equalsIgnoreCase("start")){
			if (!Permission.ADMIN_START.hasPermission(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to start a bloodmoon"));
				return true;
			}
			
			plugin.activate(worldName);
			
			for (Player worldPlayer : world.getPlayers()){
				worldPlayer.sendMessage(ChatColor.RED + "The blood moon is rising !");
			}
		}else if (option.equalsIgnoreCase("stop")){
			if (!Permission.ADMIN_STOP.hasPermission(sender)){
				sender.sendMessage(plugin.formatMessage(ChatColor.RED + "You do not have permission to stop a bloodmoon"));
				return true;
			}
			
			plugin.deactivate(worldName);
			
			for (Player worldPlayer : world.getPlayers()){
				worldPlayer.sendMessage(ChatColor.RED + "The blood moon has been stopped !");
			}
		}else{
			sender.sendMessage(plugin.formatMessage(ChatColor.RED + "Invalid option, see /" + label + " for correct usage"));
		}
		
		return true;
	}
	
}