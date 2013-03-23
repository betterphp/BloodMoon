package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.Comparator;

import net.minecraft.server.v1_5_R2.Entity;

public class DistanceComparator implements Comparator<Entity> {
	
	private Entity entity;
	
	public DistanceComparator(Entity entity){
		this.entity = entity;
	}
	
	@Override
	public int compare(Entity e1, Entity e2){
		double d0 = this.entity.e(e1);
		double d1 = this.entity.e(e2);
		
		return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
	}
	
}
