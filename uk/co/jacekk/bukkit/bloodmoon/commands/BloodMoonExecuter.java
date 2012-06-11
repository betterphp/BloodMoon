package uk.co.jacekk.bukkit.bloodmoon.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class BloodMoonExecuter extends BaseCommandExecutor<BloodMoon> {
	
	public BloodMoonExecuter(BloodMoon plugin){
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (sender instanceof Player == false){
			sender.sendMessage("Sorry the /bloodmoon command can only be used in game.");
			return true;
		}
		
		Player player = (Player) sender;
		World world = player.getWorld();
		String worldName = world.getName();
		
		if (player.hasPermission("bloodmoon.command.bloodmoon") == false){
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command !");
			return true;
		}
		
		if (BloodMoon.config.getStringList("affected-worlds").contains(worldName) == false){
			player.sendMessage(ChatColor.RED + "The blood moon is not enabled for this world !");
			return true;
		}
		
		if (BloodMoon.bloodMoonWorlds.contains(worldName)){
			BloodMoon.bloodMoonWorlds.remove(worldName);
			
			for (Player worldPlayer : world.getPlayers()){
				worldPlayer.sendMessage(ChatColor.RED + "[" + player.getName() + "] The blood moon has been stopped !");
			}
		}else{
			BloodMoon.bloodMoonWorlds.add(worldName);
			
			for (Player worldPlayer : world.getPlayers()){
				worldPlayer.sendMessage(ChatColor.RED + "[" + player.getName() + "] The blood moon is rising !");
			}
		}
		
		return true;
	}
	
}
