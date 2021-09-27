package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonParser {
	private static Gson gson = null;

	private static Gson getGson() {
		if (gson == null) {
			gson = new GsonBuilder().setPrettyPrinting().create();
		}
		return gson;
	}

	public static DirectoryJson readJson(IPath fullPath) {
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		File jsonFile = rootLoc.append(fullPath).append("acme.json").toFile();
		try {
			JsonReader reader = new JsonReader(new FileReader(jsonFile));
			DirectoryJson json = getGson().fromJson(reader, DirectoryJson.class);
			reader.close();
			return json;
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void writeJson(IPath fullPath, DirectoryJson jsonObj) {
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		File jsonFile = rootLoc.append(fullPath).append("acme.json").toFile();
		if (!jsonFile.exists()) {
			try {
				jsonFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(jsonFile));
			writer.setIndent("\t");
			getGson().toJson(jsonObj, DirectoryJson.class, writer);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
