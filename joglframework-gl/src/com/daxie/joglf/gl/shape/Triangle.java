package com.daxie.joglf.gl.shape;

/**
 * Triangle
 * @author Daba
 *
 */
public class Triangle {
	private Vertex3D[] vertices;
	
	public Triangle() {
		vertices=new Vertex3D[3];
	}
	
	public void SetVertex(int index,Vertex3D v) {
		vertices[index]=v;
	}
	
	public Vertex3D GetVertex(int index) {
		return new Vertex3D(vertices[index]);
	}
	public Vertex3D[] GetVertices() {
		Vertex3D[] ret=new Vertex3D[3];
		for(int i=0;i<3;i++)ret[i]=new Vertex3D(vertices[i]);
		
		return ret;
	}
}