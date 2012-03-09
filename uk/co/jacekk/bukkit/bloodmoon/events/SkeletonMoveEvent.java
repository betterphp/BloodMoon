package uk.co.jacekk.bukkit.bloodmoon.events;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkeletonMoveEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean isCancelled = false;
	
	private Skeleton skeleton;
	private Location from;
	private Location to;
	
	public SkeletonMoveEvent(Skeleton skeleton, Location from, Location to){
		this.skeleton = skeleton;
		this.from = from;
		this.to = to;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	public Skeleton getSkeleton(){
		return this.skeleton;
	}
	
	public Location getTo(){
		return this.to;
	}
	
	public Location getFrom(){
		return this.from;
	}
	
	public LivingEntity getTarget(){
		return skeleton.getTarget();
	}
	
	public boolean isCancelled(){
		return this.isCancelled;
	}

	public void setCancelled(boolean cancelled){
		this.isCancelled = cancelled;
	}
	
}
