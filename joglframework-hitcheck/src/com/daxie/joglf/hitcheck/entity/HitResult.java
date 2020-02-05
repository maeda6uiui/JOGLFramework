package com.daxie.joglf.hitcheck.entity;

import com.daxie.basis.vector.Vector;

/**
 * Hit result
 * @author Daba
 *
 */
public class HitResult {
	private boolean hit_flag;
	private Vector hit_position;
	
	public HitResult() {
		hit_flag=false;
		hit_position=new Vector();
	}
	
	public void SetHitFlag(boolean hit_flag) {
		this.hit_flag=hit_flag;
	}
	public void SetHitPosition(Vector hit_position) {
		this.hit_position=hit_position;
	}
	
	public boolean GetHitFlag() {
		return hit_flag;
	}
	public Vector GetHitPosition() {
		return new Vector(hit_position);
	}
}
