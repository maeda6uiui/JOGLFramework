package com.daxie.joglf.hitcheck.collider;

import com.daxie.basis.vector.Vector;
import com.daxie.basis.vector.VectorFunctions;
import com.daxie.joglf.hitcheck.HitcheckFunctions;

/**
 * Box collider
 * @author Daba
 *
 */
public class BoxCollider extends Collider{
	private Vector center;
	private Vector[] axes;
	private Vector edge_half_lengths;
	
	public BoxCollider() {
		this.SetColliderShape(ColliderShape.BOX);
		
		center=VectorFunctions.VGet(0.0f, 0.0f, 0.0f);
		
		axes=new Vector[3];
		axes[0]=VectorFunctions.VGet(1.0f, 0.0f, 0.0f);
		axes[1]=VectorFunctions.VGet(0.0f, 1.0f, 0.0f);
		axes[2]=VectorFunctions.VGet(0.0f, 0.0f, 1.0f);
		
		edge_half_lengths=VectorFunctions.VGet(1.0f, 1.0f, 1.0f);
	}
	
	public void SetBox(Vector center,Vector[] axes,Vector edge_half_lengths) {
		this.center=center;
		
		Vector[] normalized_axes=new Vector[3];
		for(int i=0;i<3;i++) {
			normalized_axes[i]=VectorFunctions.VNorm(axes[i]);
		}
		this.axes=normalized_axes;
		
		this.edge_half_lengths=edge_half_lengths;
	}
	public Vector GetCenter() {
		return new Vector(center);
	}
	public Vector[] GetAxes() {
		Vector[] ret=new Vector[3];
		
		for(int i=0;i<3;i++) {
			ret[i]=new Vector(axes[i]);
		}
		
		return ret;
	}
	public Vector GetEdgeHalfLengths() {
		return new Vector(edge_half_lengths);
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
		Vector[] vertices=collider.GetVertices();
		
		boolean ret=HitcheckFunctions.Hitcheck_OBB_Triangle(
				center, axes, edge_half_lengths, vertices[0], vertices[1], vertices[2]);
		
		return ret;
	}
	private boolean CollideWithBox(BoxCollider collider) {
		boolean ret=HitcheckFunctions.HitCheck_OBB_OBB(
				center, axes, edge_half_lengths, 
				collider.GetCenter(), collider.GetAxes(), collider.GetEdgeHalfLengths());
		
		return ret;
	}
	private boolean CollideWithSphere(SphereCollider collider) {
		Vector sphere_center=collider.GetCenter();
		float sphere_radius=collider.GetRadius();
		
		float square_distance=HitcheckFunctions.GetSquareDistance_Point_OBB(sphere_center, center, axes, edge_half_lengths);
		
		boolean ret;
		if(square_distance<=sphere_radius*sphere_radius)ret=true;
		else ret=false;
		
		return ret;
	}
	private boolean CollideWithCapsule(CapsuleCollider collider) {
		return false;
	}
}
