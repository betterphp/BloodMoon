package uk.co.jacekk.bukkit.bloodmoon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public enum Permission {
	
	ADMIN_START(				"bloodmoon.admin.start",				PermissionDefault.OP,		"Allows the player to manually start a bloodmoon"),
	ADMIN_STOP(					"bloodmoon.admin.stop",					PermissionDefault.OP,		"Allows the player to manually stop a bloodmoon"),
	ADMIN_IGNORE_WORLD_LOCK(	"bloodmoon.admin.ignore-world-lock",	PermissionDefault.OP,		"Allows the player to leave the world even if the bloodmoon is active and the lock-in-world feature is enabled");
	
	protected String node;
	protected PermissionDefault defaultValue;
	protected String description;
	
	private Permission(String node, PermissionDefault defaultValue, String description){
		this.node = node;
		this.defaultValue = defaultValue;
		this.description = description;
	}
	
	public List<Player> getPlayersWith(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for (Player player : Bukkit.getServer().getOnlinePlayers()){
			if (this.hasPermission(player)){
				players.add(player);
			}
		}
		
		return players;
	}
	
	public Boolean hasPermission(CommandSender sender){
		return sender.hasPermission(this.node);
	}
	
	public String getNode(){
		return this.node;
	}
	
	public PermissionDefault getDefault(){
		return this.defaultValue;
	}
	
	public String getDescription(){
		return this.description;
	}
	
}
