package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

	public static DirectoryJson readJson(IPath fullPath) throws IOException {
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		File jsonFile = rootLoc.append(fullPath).append("acme.json").toFile();
		JsonReader reader = new JsonReader(new FileReader(jsonFile));
		DirectoryJson json = getGson().fromJson(reader, DirectoryJson.class);
		reader.close();
		return json;
	}

	public static void writeJson(IPath fullPath, DirectoryJson jsonObj) throws IOException {
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IPath dirLoc = rootLoc.append(fullPath);
		File jsonFile = dirLoc.append("acme.json").toFile();
		if (!jsonFile.isFile()) {
			jsonFile.delete();
			dirLoc.toFile().mkdirs();
			jsonFile.createNewFile();
		}
		JsonWriter writer = new JsonWriter(new FileWriter(jsonFile));
		writer.setIndent("\t");
		getGson().toJson(jsonObj, DirectoryJson.class, writer);
		writer.close();
	}
}
