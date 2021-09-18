package org.e792a8.acme.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystem {
	private static final int TEMP_DIR_ATTEMPTS = 4096;

	// https://www.cnblogs.com/longronglang/p/7458027.html
	public static String read(File file, int length) {
//		String encoding = "UTF-8";
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
//		try {
//			return new String(filecontent, encoding);
//		} catch (UnsupportedEncodingException e) {
//			System.err.println("The OS does not support " + encoding);
//			e.printStackTrace();
//			return null;
//		}
		return new String(filecontent);
	}

	// https://www.cnblogs.com/longronglang/p/7458027.html
	public static boolean write(File file, String content) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static File createTempDir() {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = System.currentTimeMillis() + "-";
		for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
			File tempDir = new File(baseDir, baseName + counter);
			if (tempDir.mkdir()) {
				return tempDir;
			}
		}
		return null;
	}

	public static void rmDir(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					rmDir(f);
				}
			}
			file.delete();
		}
	}
}
