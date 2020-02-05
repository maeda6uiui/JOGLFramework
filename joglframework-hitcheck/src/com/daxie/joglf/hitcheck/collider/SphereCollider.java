package com.daxie.joglf.hitcheck.collider;

import com.daxie.basis.vector.Vector;
import com.daxie.basis.vector.VectorFunctions;

/**
 * Sphere collider
 * @author Daba
 *
 */
public class SphereCollider extends Collider{
	private Vector center;
	private float radius;
	
	public SphereCollider() {
		this.SetColliderShape(ColliderShape.SPHERE);
		
		center=VectorFunctions.VGet(0.0f, 0.0f, 0.0f);
		radius=1.0f;
	}
	
	public void SetSphere(Vector center,float radius) {
		this.center=center;
		this.radius=radius;
	}
	public Vector GetCenter() {
		return new Vector(center);
	}
	public float GetRadius() {
		return radius;
	}
	
	@Override
	public boolean CollideWith(Collider collider) {
		boolean ret;
		ColliderShape shape=collider.GetColliderShape();
		
		switch(shape) {
		case TRIANGLE:
			ret=this.CollideWithTriangle((TriangleCollider)collider);
			break;
		case BOX:
			ret=this.CollideWithBox((BoxCollider)collider);
			break;
		case SPHERE:
			ret=this.CollideWithSphere((SphereCollider)collider);
			break;
		case CAPSULE:
			ret=this.CollideWithCapsule((CapsuleCollider)collider);
			break;
		default:
			ret=false;
			break;
		}
		
		return ret;
	}
	private boolean CollideWithTriangle(TriangleCollider collider) {
		return false;
	}
	private boolean CollideWithBox(BoxCollider collider) {
		return false;
	}
	private boolean CollideWithSphere(SphereCollider collider) {
		return false;
	}
	private boolean CollideWithCapsule(CapsuleCollider collider) {
		return false;
	}
}
