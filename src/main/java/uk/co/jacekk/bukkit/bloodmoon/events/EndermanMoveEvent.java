package uk.co.jacekk.bukkit.bloodmoon.events;

import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EndermanMoveEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean isCancelled = false;
	
	private Enderman enderman;
	private Location from;
	private Location to;
	
	public EndermanMoveEvent(Enderman enderman, Location from, Location to){
		this.enderman = enderman;
		this.from = from;
		this.to = to;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	public Enderman getEnderman(){
		return this.enderman;
	}
	
	public Location getTo(){
		return this.to;
	}
	
	public Location getFrom(){
		return this.from;
	}
	
	public LivingEntity getTarget(){
		return enderman.getTarget();
	}
	
	public boolean isCancelled(){
		return this.isCancelled;
	}

	public void setCancelled(boolean cancelled){
		this.isCancelled = cancelled;
	}
	
}
