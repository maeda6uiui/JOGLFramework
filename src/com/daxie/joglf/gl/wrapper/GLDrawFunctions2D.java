package com.daxie.joglf.gl.wrapper;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.daxie.basis.coloru8.ColorU8;
import com.daxie.joglf.gl.front.WindowFront;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

/**
 * Draw functions for 2D primitives
 * @author Daba
 *
 */
public class GLDrawFunctions2D {
	/**
	 * Returns a normalized value ranged between -1.0 and 1.0.
	 * @param value Value
	 * @param max Maximum
	 * @return Normalized value
	 */
	private static float NormalizeCoordinate(int value,int max) {
		return 2.0f*value/max-1.0f;
	}
	/**
	 * Returns a normalized value ranged between -1.0 and 1.0.
	 * @param value Value
	 * @param max Maximum
	 * @return Normalized value
	 */
	private static float NormalizeCoordinate(float value,int max) {
		return 2.0f*value/max-1.0f;
	}
	
	public static void DrawLine2D(int x1,int y1,int x2,int y2,ColorU8 color) {
		IntBuffer pos_vbo=Buffers.newDirectIntBuffer(1);
		IntBuffer color_vbo=Buffers.newDirectIntBuffer(1);
		IntBuffer vao=Buffers.newDirectIntBuffer(1);
		
		FloatBuffer pos_buffer=Buffers.newDirectFloatBuffer(4);
		FloatBuffer color_buffer=Buffers.newDirectFloatBuffer(8);
		
		int window_width=WindowFront.GetWindowWidth();
		int window_height=WindowFront.GetWindowHeight();
		
		float normalized_x1=NormalizeCoordinate(x1, window_width);
		float normalized_y1=NormalizeCoordinate(y1, window_height);
		float normalized_x2=NormalizeCoordinate(x2, window_width);
		float normalized_y2=NormalizeCoordinate(y2, window_height);
		
		pos_buffer.put(normalized_x1);
		pos_buffer.put(normalized_y1);
		pos_buffer.put(normalized_x2);
		pos_buffer.put(normalized_y2);
		
		float color_r=color.GetR();
		float color_g=color.GetG();
		float color_b=color.GetB();
		float color_a=color.GetA();
		for(int i=0;i<2;i++) {
			color_buffer.put(color_r);
			color_buffer.put(color_g);
			color_buffer.put(color_b);
			color_buffer.put(color_a);
		}
		
		pos_buffer.flip();
		color_buffer.flip();
		
		GLShaderFunctions.EnableProgram("line_drawer");
		
		GLWrapper.glGenBuffers(1, pos_vbo);
		GLWrapper.glGenBuffers(1, color_vbo);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER, 
				Buffers.SIZEOF_FLOAT*pos_buffer.capacity(),pos_buffer,GL4.GL_STATIC_DRAW);
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, color_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER, 
				Buffers.SIZEOF_FLOAT*color_buffer.capacity(),color_buffer,GL4.GL_STATIC_DRAW);
		
		GLWrapper.glGenVertexArrays(1, vao);
		GLWrapper.glBindVertexArray(vao.get(0));
		
		//Position attribute
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(0);
		GLWrapper.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*2, 0);
		
		//Color attribute
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, color_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(1);
		GLWrapper.glVertexAttribPointer(1, 4, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*4, 0);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		GLWrapper.glBindVertexArray(0);
		
		//Draw
		GLWrapper.glBindVertexArray(vao.get(0));
		GLWrapper.glDrawArrays(GL4.GL_LINES, 0, 2);
		GLWrapper.glBindVertexArray(0);
		
		//Delete buffers
		GLWrapper.glDeleteBuffers(1, pos_vbo);
		GLWrapper.glDeleteBuffers(1, color_vbo);
		GLWrapper.glDeleteVertexArrays(1, vao);
	}
	/**
	 * Draws a rectangle.
	 * @param x1 Bottom left x-coordinate
	 * @param y1 Bottom left y-coordinate
	 * @param x2 Top right x-coordinate
	 * @param y2 Top right y-coordinate
	 * @param color Color
	 */
	public static void DrawRectangle2D(int x1,int y1,int x2,int y2,ColorU8 color) {
		IntBuffer pos_vbo=Buffers.newDirectIntBuffer(1);
		IntBuffer color_vbo=Buffers.newDirectIntBuffer(1);
		IntBuffer vao=Buffers.newDirectIntBuffer(1);
		
		FloatBuffer pos_buffer=Buffers.newDirectFloatBuffer(2*4);
		FloatBuffer color_buffer=Buffers.newDirectFloatBuffer(4*4);
		
		int window_width=WindowFront.GetWindowWidth();
		int window_height=WindowFront.GetWindowHeight();
		
		float normalized_x1=NormalizeCoordinate(x1, window_width);
		float normalized_y1=NormalizeCoordinate(y1, window_height);
		float normalized_x2=NormalizeCoordinate(x2, window_width);
		float normalized_y2=NormalizeCoordinate(y2, window_height);
		
		//Bottom left
		pos_buffer.put(normalized_x1);
		pos_buffer.put(normalized_y1);
		//Bottom right
		pos_buffer.put(normalized_x2);
		pos_buffer.put(normalized_y1);
		//Top right
		pos_buffer.put(normalized_x2);
		pos_buffer.put(normalized_y2);
		//Top left
		pos_buffer.put(normalized_x1);
		pos_buffer.put(normalized_y2);
		
		float color_r=color.GetR();
		float color_g=color.GetG();
		float color_b=color.GetB();
		float color_a=color.GetA();
		for(int i=0;i<4;i++) {
			color_buffer.put(color_r);
			color_buffer.put(color_g);
			color_buffer.put(color_b);
			color_buffer.put(color_a);
		}
		
		pos_buffer.flip();
		color_buffer.flip();
		
		GLShaderFunctions.EnableProgram("line_drawer");
		
		GLWrapper.glGenBuffers(1, pos_vbo);
		GLWrapper.glGenBuffers(1, color_vbo);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER, 
				Buffers.SIZEOF_FLOAT*pos_buffer.capacity(),pos_buffer,GL4.GL_STATIC_DRAW);
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, color_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER, 
				Buffers.SIZEOF_FLOAT*color_buffer.capacity(),color_buffer,GL4.GL_STATIC_DRAW);
		
		GLWrapper.glGenVertexArrays(1, vao);
		GLWrapper.glBindVertexArray(vao.get(0));
		
		//Position attribute
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(0);
		GLWrapper.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*2, 0);
		
		//Color attribute
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, color_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(1);
		GLWrapper.glVertexAttribPointer(1, 4, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*4, 0);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		GLWrapper.glBindVertexArray(0);
		
		//Draw
		GLWrapper.glBindVertexArray(vao.get(0));
		GLWrapper.glDrawArrays(GL4.GL_LINE_LOOP, 0, 4);
		GLWrapper.glBindVertexArray(0);
		
		//Delete buffers
		GLWrapper.glDeleteBuffers(1, pos_vbo);
		GLWrapper.glDeleteBuffers(1, color_vbo);
		GLWrapper.glDeleteVertexArrays(1, vao);
	}
	public static void DrawCircle2D(int center_x,int center_y,int radius,int div_num,ColorU8 color) {
		IntBuffer pos_vbo=Buffers.newDirectIntBuffer(1);
		IntBuffer color_vbo=Buffers.newDirectIntBuffer(1);
		IntBuffer vao=Buffers.newDirectIntBuffer(1);
		
		FloatBuffer pos_buffer=Buffers.newDirectFloatBuffer(2*div_num);
		FloatBuffer color_buffer=Buffers.newDirectFloatBuffer(4*div_num);
		
		int window_width=WindowFront.GetWindowWidth();
		int window_height=WindowFront.GetWindowHeight();
		
		for(int i=0;i<div_num;i++) {
			float th=(float)Math.PI*2.0f/div_num*i;
			
			float x=radius*(float)Math.cos(th)+center_x;
			float y=radius*(float)Math.sin(th)+center_y;
			
			float normalized_x=NormalizeCoordinate(x, window_width);
			float normalized_y=NormalizeCoordinate(y, window_height);
			
			pos_buffer.put(normalized_x);
			pos_buffer.put(normalized_y);
		}
		
		float color_r=color.GetR();
		float color_g=color.GetG();
		float color_b=color.GetB();
		float color_a=color.GetA();
		for(int i=0;i<div_num;i++) {
			color_buffer.put(color_r);
			color_buffer.put(color_g);
			color_buffer.put(color_b);
			color_buffer.put(color_a);
		}
		
		pos_buffer.flip();
		color_buffer.flip();
		
		GLShaderFunctions.EnableProgram("line_drawer");
		
		GLWrapper.glGenBuffers(1, pos_vbo);
		GLWrapper.glGenBuffers(1, color_vbo);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER, Buffers.SIZEOF_FLOAT*pos_buffer.capacity(),pos_buffer,GL4.GL_STATIC_DRAW);
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, color_vbo.get(0));
		GLWrapper.glBufferData(GL4.GL_ARRAY_BUFFER, Buffers.SIZEOF_FLOAT*color_buffer.capacity(),color_buffer,GL4.GL_STATIC_DRAW);
		
		GLWrapper.glGenVertexArrays(1, vao);
		GLWrapper.glBindVertexArray(vao.get(0));
		
		//Position attribute
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, pos_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(0);
		GLWrapper.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*2, 0);
		
		//Color attribute
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, color_vbo.get(0));
		GLWrapper.glEnableVertexAttribArray(1);
		GLWrapper.glVertexAttribPointer(1, 4, GL4.GL_FLOAT, false, Buffers.SIZEOF_FLOAT*4, 0);
		
		GLWrapper.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		GLWrapper.glBindVertexArray(0);
		
		//Draw
		GLWrapper.glBindVertexArray(vao.get(0));
		GLWrapper.glDrawArrays(GL4.GL_LINE_LOOP, 0, div_num);
		GLWrapper.glBindVertexArray(0);
		
		//Delete buffers
		GLWrapper.glDeleteBuffers(1, pos_vbo);
		GLWrapper.glDeleteBuffers(1, color_vbo);
		GLWrapper.glDeleteVertexArrays(1, vao);
	}
}