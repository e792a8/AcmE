package org.e792a8.acme.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFile {
	private static final int BUFSIZE = 4096;

	public static String read(File file, int length) {
		FileReader freader = null;
		try {
			freader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		char[] buf = new char[BUFSIZE];
		StringBuffer strbuf = new StringBuffer();
		while (length >= BUFSIZE) {
			length -= BUFSIZE;
			try {
				freader.read(buf);
				if (buf[0] == '\0') {
					freader.close();
					return strbuf.toString();
				}
				strbuf.append(buf);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					freader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		}
		if (length > 0) {
			length %= BUFSIZE;
			buf = new char[length];
			try {
				freader.read(buf);
				strbuf.append(buf);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					freader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		}
		try {
			freader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strbuf.toString();
	}

	public static boolean write(File file, String content) {
		try {
			FileWriter fwriter = new FileWriter(file);
			fwriter.write(content);
			fwriter.flush();
			fwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
