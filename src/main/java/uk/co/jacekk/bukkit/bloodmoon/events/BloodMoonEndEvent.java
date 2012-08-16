package uk.co.jacekk.bukkit.bloodmoon.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BloodMoonEndEvent extends Event {
	
private static final HandlerList handlers = new HandlerList();
	
	private World world;
	
	public BloodMoonEndEvent(World world){
		this.world = world;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	public World getWorld(){
		return this.world;
	}
	
}
