package com.daxie.joglf.gl.drawer;

import java.awt.Point;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.TreeMap;

import com.daxie.basis.coloru8.ColorU8;
import com.daxie.joglf.gl.shape.Vertex2D;
import com.daxie.joglf.gl.wrapper.GLShaderFunctions;
import com.daxie.joglf.gl.wrapper.GLWrapper;
import com.daxie.log.LogFile;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

/**
 * Draws filled triangles.
 * @author Daba
 *
 */
public class Dynamic2DFilledTrianglesDrawer {
	private Map<Integer, Vertex2D[]>triangles_map;
	
	private IntBuffer pos_vbo;
	private IntBuffer dif_vbo;
	private IntBuffer vao;
	
	private int window_width;
	private int window_height;
	
	public Dynamic2DFilledTrianglesDrawer() {
		triangles_map=new TreeMap<>();
		
		pos_vbo=Buffers.newDirectIntBuffer(1);
		dif_vbo=Buffers.newDirectIntBuffer(1);
		vao=Buffers.newDirectIntBuffer(1);
		
		GLWrapper.glGenBuffers(1, pos_vbo);
		GLWrapper.glGenBuffers(1, dif_vbo);
		GLWrapper.glGenVertexArrays(1, vao);
		
		window_width=640;
		window_height=480;
	}
	
	public void UpdateBuffers() {
		int triangle_num=triangles_map.size();
		int point_num=triangle_num*3;
		
		FloatBuffer pos_buffer=Buffers.newDirectFloatBuffer(point_num*2);
		FloatBuffer dif_buffer=Buffers.newDirectFloatBuffer(point_num*4);
		
		for(Vertex2D[] triangle:triangles_map.values()) {
			for(int i=0;i<3;i++) {
				Point point=triangle[i].GetPoint();
				ColorU8 dif=triangle[i].GetColor();
				
				int x=point.x;
				int y=point.y;
				float normalized_x=2.0f*x/window_width-1.0f;
				float normalized_y=2.0f*y/window_height-1.0f;
				
				pos_buffer.put(normalized_x);
				pos_buffer.put(normalized_y);
				dif_buffer.put(dif.GetR());
				dif_buffer.put(dif.GetG());
				dif_buffer.put(dif.GetB());
				dif_buffer.put(dif.GetA());
			}
		}
		
		((Buffer)pos_buffer).flip();
		((Buffer)dif_buffer).flip();
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER, 
				Buffers.SIZEOF_FLOAT*pos_buffer.capacity(), pos_buffer, GL4.GL_DYNAMIC_DRAW);
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, dif_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER,
				Buffers.SIZEOF_FLOAT*dif_buffer.capacity(), dif_buffer, GL4.GL_DYNAMIC_DRAW);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		
		GLWrapper.glBindVertexArray(vao.get(0));
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(0);
		GLWrapper.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*2, 0);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, dif_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(1);
		GLWrapper.glVertexAttribPointer(1, 4, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*4, 0);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		GLWrapper.glBindVertexArray(0);
	}
	public void DeleteBuffers() {
		GLWrapper.glDeleteBuffers(1, pos_vbo);
		GLWrapper.glDeleteBuffers(1, dif_vbo);
		GLWrapper.glDeleteVertexArrays(1, vao);
	}
	
	public void AddTriangle(int triangle_id,Vertex2D v1,Vertex2D v2,Vertex2D v3) {
		Vertex2D[] vertices=new Vertex2D[3];
		vertices[0]=v1;
		vertices[1]=v2;
		vertices[2]=v3;
		
		triangles_map.put(triangle_id, vertices);
	}
	public int DeleteTriangle(int triangle_id) {
		if(triangles_map.containsKey(triangle_id)==false) {
			LogFile.WriteWarn("[Dynamic2DFilledTrianglesDrawer-DeleteTriangle] ", true);
			LogFile.WriteWarn("No such triangle. triangle_id:"+triangle_id, false);
			
			return -1;
		}
		
		triangles_map.remove(triangle_id);
		
		return 0;
	}
	public void DeleteAllTriangles() {
		triangles_map.clear();
	}
	
	public void SetWindowSize(int width,int height) {
		window_width=width;
		window_height=height;
	}
	
	public void Draw() {
		GLShaderFunctions.EnableProgram("line_drawer");
		
		GLWrapper.glBindVertexArray(vao.get(0));
		
		int triangle_num=triangles_map.size();
		int point_num=triangle_num*3;
		
		GLWrapper.glEnable(GL4.GL_BLEND);
		GLWrapper.glDrawArrays(GL4.GL_TRIANGLES, 0, point_num);
		GLWrapper.glDisable(GL4.GL_BLEND);
		
		GLWrapper.glBindVertexArray(0);
	}
}
