package uk.co.jacekk.bukkit.bloodmoon.pathfinders;

import java.util.Comparator;

import net.minecraft.server.Entity;

public class BloodMoonDistanceComparator implements Comparator<Entity> {
	
	private Entity b;
	
	public BloodMoonDistanceComparator(Entity b){
		this.b = b;
	}
	
	public int compare(Entity e1, Entity e2){
		double d0 = this.b.j(e1);
		double d1 = this.b.j(e2);
		
		return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
	}
	
}
