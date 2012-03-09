package uk.co.jacekk.bukkit.bloodmoon.events;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreeperMoveEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean isCancelled = false;
	
	private Creeper creeper;
	private Location from;
	private Location to;
	
	public CreeperMoveEvent(Creeper creeper, Location from, Location to){
		this.creeper = creeper;
		this.from = from;
		this.to = to;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	public Creeper getCreeper(){
		return this.creeper;
	}
	
	public Location getTo(){
		return this.to;
	}
	
	public Location getFrom(){
		return this.from;
	}
	
	public LivingEntity getTarget(){
		return creeper.getTarget();
	}
	
	public boolean isCancelled(){
		return this.isCancelled;
	}

	public void setCancelled(boolean cancelled){
		this.isCancelled = cancelled;
	}
	
}
