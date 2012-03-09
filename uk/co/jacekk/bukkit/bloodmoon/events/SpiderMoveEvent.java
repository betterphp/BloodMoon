package uk.co.jacekk.bukkit.bloodmoon.events;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpiderMoveEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean isCancelled = false;
	
	private Spider spider;
	private Location from;
	private Location to;
	
	public SpiderMoveEvent(Spider spider, Location from, Location to){
		this.spider = spider;
		this.from = from;
		this.to = to;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	public Spider getSpider(){
		return this.spider;
	}
	
	public Location getTo(){
		return this.to;
	}
	
	public Location getFrom(){
		return this.from;
	}
	
	public LivingEntity getTarget(){
		return spider.getTarget();
	}
	
	public boolean isCancelled(){
		return this.isCancelled;
	}

	public void setCancelled(boolean cancelled){
		this.isCancelled = cancelled;
	}
	
}