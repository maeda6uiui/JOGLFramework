package com.github.dabasan.joglf.gl.model.loader.assimp;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dabasan.basis.coloru8.ColorU8Functions;
import com.github.dabasan.jassimp.AiColor;
import com.github.dabasan.jassimp.AiMaterial;
import com.github.dabasan.jassimp.AiMesh;
import com.github.dabasan.jassimp.AiPostProcessSteps;
import com.github.dabasan.jassimp.AiScene;
import com.github.dabasan.jassimp.AiTextureType;
import com.github.dabasan.jassimp.Jassimp;
import com.github.dabasan.joglf.gl.model.buffer.BufferedVertices;
import com.github.dabasan.joglf.gl.texture.TextureMgr;
import com.github.dabasan.tool.FilenameFunctions;

/**
 * Loads a 3D model with Assimp.
 * 
 * @author Daba
 *
 */
public class AssimpLoader {
	private static Logger logger = LoggerFactory.getLogger(AssimpLoader.class);

	public static List<BufferedVertices> LoadModelWithAssimp(String model_filename,
			Set<AiPostProcessSteps> flags) throws IOException {
		logger.info("Start loading a model with Assimp. model_filename={}", model_filename);

		List<BufferedVertices> buffered_vertices_list = new ArrayList<>();
		String model_directory = FilenameFunctions.GetFileDirectory(model_filename);

		AiScene scene = Jassimp.importFile(model_filename, flags);
		List<AiMaterial> materials = scene.getMaterials();
		List<AiMesh> meshes = scene.getMeshes();

		if (materials.size() == 0) {
			throw new IOException("There are no materials in this model.");
		}
		if (meshes.size() == 0) {
			throw new IOException("There are no meshes in this model.");
		}
		if (materials.size() != meshes.size()) {
			throw new IOException("material_num != mesh_num");
		}

		for (var material : materials) {
			BufferedVertices buffered_vertices = new BufferedVertices();

			String texture_filename = material.getTextureFile(AiTextureType.DIFFUSE, 0);
			buffered_vertices.SetDiffuseTextureMap(texture_filename);

			if (texture_filename != null && texture_filename.equals("") == false) {
				texture_filename = model_directory + "/" + texture_filename;

				int texture_handle = TextureMgr.LoadTexture(texture_filename);
				buffered_vertices.SetTextureHandle(texture_handle);
			} else {
				buffered_vertices.SetTextureHandle(-1);
			}

			AiColor ambient_color = material.getAmbientColor(null);
			AiColor diffuse_color = material.getDiffuseColor(null);
			AiColor specular_color = material.getSpecularColor(null);
			float shininess = material.getShininess();
			float opacity = material.getOpacity();
			buffered_vertices.SetAmbientColor(ColorU8Functions.GetColorU8(ambient_color.getRed(),
					ambient_color.getGreen(), ambient_color.getBlue(), ambient_color.getAlpha()));
			buffered_vertices.SetDiffuseColor(ColorU8Functions.GetColorU8(diffuse_color.getRed(),
					diffuse_color.getGreen(), diffuse_color.getBlue(), diffuse_color.getAlpha()));
			buffered_vertices.SetSpecularColor(
					ColorU8Functions.GetColorU8(specular_color.getRed(), specular_color.getGreen(),
							specular_color.getBlue(), specular_color.getAlpha()));
			buffered_vertices.SetSpecularExponent(shininess);
			buffered_vertices.SetDissolve(opacity);

			buffered_vertices_list.add(buffered_vertices);
		}

		for (int i = 0; i < meshes.size(); i++) {
			AiMesh mesh = meshes.get(i);
			BufferedVertices buffered_vertices = buffered_vertices_list.get(i);

			IntBuffer indices_buffer = mesh.getIndexBuffer();
			FloatBuffer pos_buffer = mesh.getPositionBuffer();
			FloatBuffer norm_buffer = mesh.getNormalBuffer();
			FloatBuffer uv_buffer = mesh.getTexCoordBuffer(0);

			buffered_vertices.SetIndicesBuffer(indices_buffer);
			buffered_vertices.SetPosBuffer(pos_buffer);
			buffered_vertices.SetNormBuffer(norm_buffer);
			buffered_vertices.SetUVBuffer(uv_buffer);
		}

		return buffered_vertices_list;
	}
	public static List<BufferedVertices> LoadModelWithAssimp(String model_filename)
			throws IOException {
		return LoadModelWithAssimp(model_filename,
				EnumSet.of(AiPostProcessSteps.TRIANGULATE, AiPostProcessSteps.GEN_NORMALS,
						AiPostProcessSteps.FIND_INVALID_DATA,
						AiPostProcessSteps.FIX_INFACING_NORMALS, AiPostProcessSteps.OPTIMIZE_MESHES,
						AiPostProcessSteps.VALIDATE_DATA_STRUCTURE,
						AiPostProcessSteps.REMOVE_REDUNDANT_MATERIALS));
	}
}
