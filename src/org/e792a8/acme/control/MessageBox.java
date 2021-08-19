package org.e792a8.acme.control;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.wb.swt.SWTResourceManager;

public class MessageBox {
	static MessageConsole console;
	static Map<String, MessageConsoleStream> streams;

	public static void setBox(MessageConsole box) {
		console = box;
		streams = new HashMap<>();
		MessageConsoleStream stream = console.newMessageStream();
		stream.setColor(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		streams.put("BLACK", stream);
		stream = console.newMessageStream();
		stream.setColor(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		streams.put("GREEN", stream);
		stream = console.newMessageStream();
		stream.setColor(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		streams.put("RED", stream);
	}

	public static void printMsg(String color, String msg) {
		String text = String.format("[%tT] ", Calendar.getInstance());
		MessageConsoleStream stream = streams.get("BLACK");
		if (stream != null)
			stream.print(text);
		stream = streams.get(color);
		if (stream == null)
			stream = streams.get("BLACK");
		if (stream != null)
			stream.println(msg);
	}

	public static void clear() {
		console.clearConsole();
	}
}
