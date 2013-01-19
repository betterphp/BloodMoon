package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.Comparator;

import net.minecraft.server.v1_4_R1.Entity;

public class BloodMoonDistanceComparator implements Comparator<Entity> {
	
	private Entity b;
	
	public BloodMoonDistanceComparator(Entity b){
		this.b = b;
	}
	
	@Override
	public int compare(Entity e1, Entity e2){
		double d0 = this.b.e(e1);
		double d1 = this.b.e(e2);
		
		return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
	}
	
}
